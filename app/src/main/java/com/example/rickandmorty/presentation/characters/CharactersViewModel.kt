package com.example.rickandmorty.presentation.characters

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.useCase.GetCharactersUseCase
import com.example.rickandmorty.domain.useCase.favourites.FavouritesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
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
    private var isLoading: Boolean = false


    private fun fetchData(callback: () -> Unit = {}) {
        viewModelScope.launch {
            when (state.value.screenMode) {
                ScreenMode.ALL -> fetchAllCharacters()
                ScreenMode.FAVOURITES -> fetchFavouritesCharacters()
            }
            callback()
        }
    }

    fun onEvent(event: CharactersScreenEvent) {
        when (event) {
            is CharactersScreenEvent.Refresh -> {
                resetScreenState(state.value.copy(isRefreshing = true))
                fetchData() {
                    _state.value = state.value.copy(isRefreshing = false)
                }
            }

            is CharactersScreenEvent.FetchNextPage -> {
                fetchData()
            }

            is CharactersScreenEvent.ChangeScreenMode -> {
                if (event.mode != state.value.screenMode) {
                    viewModelScope.launch {
                        _state.value = state.value.copy(
                            screenMode = event.mode,
                            isRefreshing = false
                        )
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
        if (!isLoading) {
            if (!state.value.isMoreDataAll) return
            isLoading = true
            val nextPage = page + 1
            getCharactersUseCase.invoke(nextPage).onSuccess {
                _state.value = state.value.copy(
                    characters = state.value.characters + it.characters,
                    isMoreDataAll = it.totalPages > nextPage,
                    isError = false
                )
                page = nextPage
                isLoading = false
            }.onFailure {
                isLoading = false
                resetScreenState(state.value.copy(isError = true))
            }
        }
    }

    private suspend fun fetchFavouritesCharacters() {
        favouritesUseCases.getFavouritesUseCase().catch {
            resetScreenState(state.value.copy(isError = true))
        }.collect { result ->
            _state.value = state.value.copy(
                favourites = result, isError = false, isMoreDataFav = false, isRefreshing = false
            )
        }
    }

    private fun resetScreenState(state: CharactersScreenState) {
        page = 0
        with(state) {
            _state.value = when (screenMode) {
                ScreenMode.ALL -> state.copy(
                    isMoreDataAll = true,
                    screenMode = screenMode,
                    favourites = favourites,
                    characters = emptyList()
                )

                ScreenMode.FAVOURITES -> state.copy(
                    isMoreDataFav = true,
                    screenMode = screenMode,
                    characters = characters,
                    favourites = emptyList()
                )
            }
        }

    }

}