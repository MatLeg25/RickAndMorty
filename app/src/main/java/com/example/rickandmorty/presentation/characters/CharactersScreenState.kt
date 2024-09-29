package com.example.rickandmorty.presentation.characters

import com.example.rickandmorty.domain.model.Character

data class CharactersScreenState(
    val screenMode: ScreenMode = ScreenMode.ALL,
    val characters: List<Character> = emptyList(),
    val favourites: List<Character> = emptyList(),
    val isMoreData: Boolean = true,
    val isError: Boolean = false,
)