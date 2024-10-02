package com.example.rickandmorty

import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onParent
import androidx.compose.ui.test.onSibling
import androidx.compose.ui.test.onSiblings
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.printToLog
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import com.example.rickandmorty.data.repository.RickAndMortyRepositoryFake
import com.example.rickandmorty.domain.useCase.GetCharactersUseCase
import com.example.rickandmorty.domain.useCase.favourites.AddFavouriteUseCase
import com.example.rickandmorty.domain.useCase.favourites.DeleteFavouriteUseCase
import com.example.rickandmorty.domain.useCase.favourites.FavouritesUseCases
import com.example.rickandmorty.domain.useCase.favourites.GetFavouritesUseCase
import com.example.rickandmorty.presentation.characters.CharactersScreen
import com.example.rickandmorty.presentation.characters.CharactersViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class AddDeleteFavouriteCharacterE2E {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var repositoryFake: RickAndMortyRepositoryFake
    private lateinit var viewModelFake: CharactersViewModel

    private val buttonMatcher = SemanticsMatcher.expectValue(
        SemanticsProperties.Role, Role.Button
    )


    @Before
    fun setUp() {
        repositoryFake = RickAndMortyRepositoryFake()
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
        val lastItemIndex = Config.CHARACTERS_TEST_LIST.lastIndex
        val secondItemName = Config.CHARACTER_NAME_PREFIX + "1"
        val lastItemName = Config.CHARACTER_NAME_PREFIX + lastItemIndex

        //check UI updates when change ScreenMode
        composeRule
            .onNodeWithText(text = getString(R.string.all))
            .performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
        //wait till load data (suspend fun)
        composeRule.waitUntil(timeoutMillis = 1000) {
            composeRule
                .onAllNodesWithTag(getString(R.string.tag_add_delete_favourite))
                .fetchSemanticsNodes().isNotEmpty()
        }
        //test adding to favourites
        //add second item
        composeRule.onAllNodesWithTag(getString(R.string.tag_add_delete_favourite)).get(1)
            .performClick()
        //add last element from the list
        composeRule.onNodeWithTag(getString(R.string.tag_lazy_list))
            .performScrollToIndex(Config.CHARACTERS_TEST_LIST.lastIndex)
        composeRule.onNodeWithText(lastItemName, useUnmergedTree = true).onSiblings().onLast()
            .performClick()
        //check if second item keep selected favourite icon
        composeRule.onNodeWithTag(getString(R.string.tag_lazy_list))
            .performScrollToIndex(0)
        composeRule.onAllNodesWithTag(
            getString(R.string.tag_add_delete_favourite),
            useUnmergedTree = true
        ).get(1)
            .onChild().assert(
                hasContentDescriptionExactly(getString(R.string.delete_favourite))
            )
        //change ScreenMode to favourite
        composeRule
            .onNodeWithText(text = getString(R.string.favourite))
            .performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
        //check if favourite screen contains two selected items with updated icons
        composeRule.onAllNodesWithTag(getString(R.string.tag_add_delete_favourite))
            .assertCountEquals(2)
        composeRule.onNodeWithText(secondItemName).assertIsDisplayed()
        composeRule.onNodeWithText(secondItemName, useUnmergedTree = true).onSiblings().onLast()
            .onChild()
            .assert(hasContentDescriptionExactly(getString(R.string.delete_favourite)))
        composeRule.onNodeWithText(lastItemName).assertIsDisplayed()
        composeRule.onNodeWithText(lastItemName, useUnmergedTree = true).onSiblings().onLast()
            .onChild()
            .assert(hasContentDescriptionExactly(getString(R.string.delete_favourite)))
        //remove from favourite screen
        composeRule.onAllNodesWithTag(getString(R.string.tag_add_delete_favourite))
            .filter(buttonMatcher).onFirst().performClick()
        composeRule.onAllNodesWithTag(getString(R.string.tag_add_delete_favourite))
            .assertCountEquals(1)
        composeRule.onNodeWithText(lastItemName).assertExists()
        //change screen mode to all
        composeRule
            .onNodeWithText(text = getString(R.string.all))
            .performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
        //remove last element from the list
        composeRule.onNodeWithTag(getString(R.string.tag_lazy_list))
            .performScrollToIndex(Config.CHARACTERS_TEST_LIST.lastIndex)
        //check if last item keep selected favourite icon
        composeRule.onAllNodesWithTag(
            getString(R.string.tag_add_delete_favourite),
            useUnmergedTree = true
        ).onLast()
            .onChild().assert(
                hasContentDescriptionExactly(getString(R.string.delete_favourite))
            )
        //remove last item from favourites
        composeRule.onNodeWithText(lastItemName, useUnmergedTree = true).onSiblings().onLast()
            .performClick()
        //change ScreenMode to favourite
        composeRule
            .onNodeWithText(text = getString(R.string.favourite))
            .performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
        composeRule.onAllNodesWithTag(getString(R.string.tag_add_delete_favourite))
            .assertCountEquals(0)
        //change screen mode to all
        composeRule
            .onNodeWithText(text = getString(R.string.all))
            .performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
    }

    private fun getString(@StringRes stringRes: Int): String {
        return composeRule.activity.getString(stringRes)
    }

}