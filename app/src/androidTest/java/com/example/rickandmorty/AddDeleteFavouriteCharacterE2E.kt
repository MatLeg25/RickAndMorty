package com.example.rickandmorty

import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.example.rickandmorty.data.repository.RickAndMortyRepositoryFake
import com.example.rickandmorty.domain.useCase.GetCharactersUseCase
import com.example.rickandmorty.domain.useCase.favourites.AddFavouriteUseCase
import com.example.rickandmorty.domain.useCase.favourites.DeleteFavouriteUseCase
import com.example.rickandmorty.domain.useCase.favourites.FavouritesUseCases
import com.example.rickandmorty.domain.useCase.favourites.GetFavouritesUseCase
import com.example.rickandmorty.presentation.characters.CharactersScreen
import com.example.rickandmorty.presentation.characters.CharactersViewModel
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddDeleteFavouriteCharacterE2E {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var repositoryFake: RickAndMortyRepositoryFake
    private lateinit var viewModelFake: CharactersViewModel

    private var characterNamePrefix = ""
    private val buttonMatcher = SemanticsMatcher.expectValue(
        SemanticsProperties.Role, Role.Button
    )


    @Before
    fun setUp() {
        characterNamePrefix = getString(R.string.tag_list_item_prefix)
        repositoryFake = RickAndMortyRepositoryFake(characterNamePrefix)
        viewModelFake = CharactersViewModel(
            getCharactersUseCase = GetCharactersUseCase(repositoryFake),
            favouritesUseCases = FavouritesUseCases(
                getFavouritesUseCase = GetFavouritesUseCase(repositoryFake),
                addFavouriteUseCase = AddFavouriteUseCase(repositoryFake),
                deleteFavouriteUseCase = DeleteFavouriteUseCase(repositoryFake)
            ),
        )

        composeRule.activity.setContent {
            RickAndMortyTheme {
                CharactersScreen(viewModel = viewModelFake)
            }
        }
    }


    @Test
    fun add_and_delete_items_favourite_correctly_update_ui() {
        val item1Index = 1
        val item2Index = repositoryFake.characters.lastIndex - 1 //second from the end
        val item1Name = characterNamePrefix + item1Index
        val item2Name = characterNamePrefix + item2Index
        val item1Tag = item1Name
        val item2Tag = item2Name

        //check UI updates when change ScreenMode
        composeRule.onNodeWithText(text = getString(R.string.all)).performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
        //wait till load data (suspend fun)
        composeRule.onAllNodesWithTag(getString(R.string.tag_add_delete_favourite))
            .fetchSemanticsNodes().isNotEmpty()
        //test adding to favourites
        //add item1 to favourites
        composeRule.onAllNodesWithTag(getString(R.string.tag_add_delete_favourite)).get(item1Index)
            .performClick()
        //add item2 to favourites
        composeRule.onNodeWithTag(getString(R.string.tag_lazy_list))
            .performScrollToIndex(item2Index)
        composeRule.onNodeWithTag(item2Tag).onChildren().onLast().performClick()
        //check if item1 keep selected favourite icon
        composeRule.onNodeWithTag(getString(R.string.tag_lazy_list))
            .performScrollToIndex(item1Index)
        composeRule.onNodeWithTag(item1Tag).onChildren().onLast().assert(
            hasContentDescriptionExactly(getString(R.string.delete_favourite))
        )
        //change ScreenMode to favourite
        composeRule.onNodeWithText(text = getString(R.string.favourite)).performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
        //check if favourite screen contains two selected items with updated icons
        composeRule.onAllNodesWithTag(getString(R.string.tag_add_delete_favourite))
            .assertCountEquals(2)
        composeRule.onNodeWithText(item1Name).assertIsDisplayed()
        composeRule.onNodeWithTag(item1Tag).onChildren().onLast().assert(
            hasContentDescriptionExactly(getString(R.string.delete_favourite))
        )
        composeRule.onNodeWithText(item2Name).assertIsDisplayed()
        composeRule.onNodeWithTag(item2Tag).onChildren().onLast().assert(
            hasContentDescriptionExactly(getString(R.string.delete_favourite))
        )
        //remove item1 from favourite screen
        composeRule.onNodeWithTag(item1Tag).onChildren().onLast().performClick()
        composeRule.onAllNodesWithTag(getString(R.string.tag_add_delete_favourite))
            .assertCountEquals(1)
        composeRule.onNodeWithText(item2Name).assertExists()
        //change screen mode to all
        composeRule.onNodeWithText(text = getString(R.string.all)).performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
        //remove item2 from the list
        composeRule.onNodeWithTag(getString(R.string.tag_lazy_list))
            .performScrollToIndex(item2Index)
        //check if item2 keep selected favourite icon
        composeRule.onNodeWithTag(item2Tag).onChildren().onLast().assert(
            hasContentDescriptionExactly(getString(R.string.delete_favourite))
        )
        //remove item2 from favourites
        composeRule.onNodeWithTag(item2Tag).onChildren().onLast().performClick()
        //change ScreenMode to favourite
        composeRule.onNodeWithText(text = getString(R.string.favourite)).performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
        composeRule.onAllNodesWithTag(getString(R.string.tag_add_delete_favourite))
            .assertCountEquals(0)
        //change screen mode to all
        composeRule.onNodeWithText(text = getString(R.string.all)).performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
    }

    private fun getString(@StringRes stringRes: Int): String {
        return composeRule.activity.getString(stringRes)
    }

}