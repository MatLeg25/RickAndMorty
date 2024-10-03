package com.example.rickandmorty.presentation.characters

import com.example.rickandmorty.domain.model.Character

data class CharactersScreenState(
    val screenMode: ScreenMode = ScreenMode.ALL,
    val characters: List<Character> = emptyList(),
    val favourites: List<Character> = emptyList(),
    val isRefreshing: Boolean = false,
    val isMoreDataAll: Boolean = true,
    val isMoreDataFav: Boolean = true,
    val isError: Boolean = false,
) {
    fun isMoreData(): Boolean {
        return if (screenMode == ScreenMode.ALL) isMoreDataAll else isMoreDataFav
    }
}