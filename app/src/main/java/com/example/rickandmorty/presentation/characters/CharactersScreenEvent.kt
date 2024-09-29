package com.example.rickandmorty.presentation.characters

sealed class CharactersScreenEvent {
    object Refresh: CharactersScreenEvent()
    object FetchNextPage: CharactersScreenEvent()
    data class ChangeScreenMode(val mode: ScreenMode): CharactersScreenEvent()
}