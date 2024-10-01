package com.example.rickandmorty.presentation.characters

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.useCase.GetCharactersUseCase
import com.example.rickandmorty.domain.useCase.favourites.FavouritesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val favouritesUseCases: FavouritesUseCases
) : ViewModel() {

    private var _state = mutableStateOf(CharactersScreenState())
    val state: State<CharactersScreenState> = _state

    private var page: Int = 0


    private fun fetchData() {
        println(">>>> fetchData for ${state.value.screenMode}")
        viewModelScope.launch {
            when (state.value.screenMode) {
                ScreenMode.ALL -> fetchAllCharacters()
                ScreenMode.FAVOURITES -> fetchFavouritesCharacters()
            }
        }
    }

    fun onEvent(event: CharactersScreenEvent) {
        when (event) {
            is CharactersScreenEvent.Refresh -> {
                resetScreenState()
                fetchData()
            }

            is CharactersScreenEvent.FetchNextPage -> {
                fetchData()
            }

            is CharactersScreenEvent.ChangeScreenMode -> {
                if (event.mode != state.value.screenMode) {
                    viewModelScope.launch {
                        //refresh Favourite list when user go to Favourite screen
                        if (event.mode == ScreenMode.FAVOURITES) fetchFavouritesCharacters()
                        _state.value = state.value.copy(screenMode = event.mode)
                    }
                }
            }

            is CharactersScreenEvent.AddDeleteFavourite -> {
                viewModelScope.launch {
                    val updatedCharacters = state.value.characters.toMutableList()
                    if (event.character.isFavourite) {
                        favouritesUseCases.deleteFavouriteUseCase(event.character)
                    } else {
                        favouritesUseCases.addFavouriteUseCase(event.character)
                    }
                    fetchFavouritesCharacters()
                    //update item from all characters local list
                    with(updatedCharacters) {
                        indexOf(event.character).takeIf { it > -1 }?.let { idx ->
                            removeAt(idx)
                            add(
                                idx,
                                event.character.copy(isFavourite = !event.character.isFavourite)
                            )
                        }
                        _state.value = state.value.copy(characters = this)
                    }
                }
            }

        }
    }

    private suspend fun fetchAllCharacters() {
        if (!state.value.isLoading) {
            if (!state.value.isMoreData) return
            _state.value = state.value.copy(isLoading = true)
            val nextPage = page + 1
            getCharactersUseCase.invoke(nextPage)
                .onSuccess {
                    _state.value =
                        state.value.copy(
                            characters = state.value.characters + it.characters,
                            isMoreData = it.totalPages > nextPage,
                            isError = false
                        )
                    page = nextPage
                    _state.value = state.value.copy(isLoading = false)
                }
                .onFailure {
                    _state.value = state.value.copy(isError = true, isLoading = false)
                }
        }
    }

    private suspend fun fetchFavouritesCharacters() {
        favouritesUseCases.getFavouritesUseCase()
            .onSuccess {
                _state.value =
                    state.value.copy(
                        favourites = it,
                        isMoreData = false,
                        isError = false
                    )
            }
            .onFailure {
                _state.value = state.value.copy(isError = true)
            }
    }

    private fun resetScreenState() {
        page = 0
        with(state.value) {
            _state.value = when (screenMode) {
                ScreenMode.ALL -> CharactersScreenState(
                    screenMode = screenMode,
                    favourites = favourites
                )

                ScreenMode.FAVOURITES -> CharactersScreenState(
                    screenMode = screenMode,
                    characters = favourites
                )
            }
        }

    }

}