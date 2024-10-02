package com.example.rickandmorty

import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import com.example.rickandmorty.data.repository.RickAndMortyRepositoryFake
import com.example.rickandmorty.presentation.characters.CharactersScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddDeleteFavouriteCharacterE2E {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var repositoryFake: RickAndMortyRepositoryFake

    @Before
    fun setUp() {
        repositoryFake = RickAndMortyRepositoryFake()

        composeRule.activity.setContent {
            RickAndMortyTheme {
                CharactersScreen()
            }
        }
    }

    @Test
    fun add_and_delete_items_favourite_correctly_update_ui() {
        composeRule
            .onNodeWithText(text = getString(R.string.all))
            .performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
        composeRule
            .onNodeWithText(text = getString(R.string.all))
            .performClick()
            .assert(hasTestTag(getString(R.string.tag_has_border)))
        composeRule
            .onNodeWithText(text = getString(R.string.favourite))
            .assert(hasTestTag(getString(R.string.tag_has_border))).assertDoesNotExist()
        composeRule.onAllNodesWithTag((getString(R.string.tag_add_delete_favourite))).get(2)
            .performClick()
        composeRule.onNodeWithText(Config.CHARACTER_NAME_PREFIX + Config.CHARACTERS_TEST_LIST.lastIndex)
            .performScrollToIndex(Config.CHARACTERS_TEST_LIST.lastIndex)
        composeRule.onNodeWithText(Config.CHARACTER_NAME_PREFIX + Config.CHARACTERS_TEST_LIST.lastIndex)
            .assertExists()

    }

    private fun getString(@StringRes stringRes: Int): String {
        return composeRule.activity.getString(stringRes)
    }

}