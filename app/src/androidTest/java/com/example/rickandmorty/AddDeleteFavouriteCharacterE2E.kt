package com.example.rickandmorty

import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
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
        val secondItemName = Config.CHARACTER_NAME_PREFIX + "0"
        val lastItemName = Config.CHARACTER_NAME_PREFIX + lastItemIndex

        //check UI updates when change ScreenMode
        composeRule
            .onNodeWithText(text = getString(R.string.all))
            .performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
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
        composeRule.onAllNodesWithTag((getString(R.string.tag_add_delete_favourite))).get(1)
            .performClick()
        //add last element from the list
        composeRule.onNodeWithTag(getString(R.string.tag_lazy_list))
            .performScrollToIndex(Config.CHARACTERS_TEST_LIST.lastIndex)
        composeRule.onNodeWithText(lastItemName)
            .assertExists()
            .performClick()

    }

    private fun getString(@StringRes stringRes: Int): String {
        return composeRule.activity.getString(stringRes)
    }

}