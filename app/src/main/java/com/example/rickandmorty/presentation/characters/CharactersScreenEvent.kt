package com.example.rickandmorty.presentation.characters

import com.example.rickandmorty.domain.model.Character

sealed class CharactersScreenEvent {
    data object Refresh: CharactersScreenEvent()
    data object FetchNextPage: CharactersScreenEvent()
    data class ChangeScreenMode(val mode: ScreenMode): CharactersScreenEvent()
    data class AddDeleteFavourite(val character: Character): CharactersScreenEvent()
}