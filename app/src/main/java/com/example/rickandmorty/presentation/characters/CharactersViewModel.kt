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

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            when (state.value.screenMode) {
                ScreenMode.ALL -> fetchAllCharacters()
                ScreenMode.FAVOURITES -> fetchFavouritesCharacters()
            }
        }
    }

    fun onEvent(event: CharactersScreenEvent) {
        when (event) {
            is CharactersScreenEvent.Refresh -> {} //todo pull refresh
            is CharactersScreenEvent.FetchNextPage -> {}
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
        val nextPage = page + 1
        getCharactersUseCase.invoke(nextPage)
            .onSuccess {
                _state.value =
                    state.value.copy(
                        characters = state.value.characters + it.characters,
                        isMoreData = it.totalPages == page,
                        isError = false
                    )
                page = nextPage
            }
            .onFailure {
                _state.value = state.value.copy(isError = true)
            }
    }

    private suspend fun fetchFavouritesCharacters() {
        println(">>>> fetchFavouritesCharacters")
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
                println(">>>>>>>>>>>> response NOK= ${it.message}")
                _state.value = state.value.copy(isError = true)
            }
    }

}