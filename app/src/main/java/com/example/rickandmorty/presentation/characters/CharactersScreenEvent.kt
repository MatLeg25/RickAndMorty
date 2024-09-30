package com.example.rickandmorty.presentation.characters

import com.example.rickandmorty.domain.model.Character

sealed class CharactersScreenEvent {
    data object Refresh: CharactersScreenEvent()
    data object FetchNextPage: CharactersScreenEvent()
    data class ChangeScreenMode(val mode: ScreenMode): CharactersScreenEvent()
    data class AddFavourite(val character: Character): CharactersScreenEvent()
    data class DeleteFavourite(val character: Character): CharactersScreenEvent()
}