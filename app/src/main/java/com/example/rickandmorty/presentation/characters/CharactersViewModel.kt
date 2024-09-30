package com.example.rickandmorty.presentation.characters

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.useCase.GetCharactersUseCase
import com.example.rickandmorty.domain.useCase.favourites.DeleteFavouriteUseCase
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
                    _state.value = state.value.copy(screenMode = event.mode)
                }
            }

            is CharactersScreenEvent.AddFavourite -> {
                viewModelScope.launch {
                    favouritesUseCases.addFavouriteUseCase(event.character)
                }
            }

            is CharactersScreenEvent.DeleteFavourite -> {
                viewModelScope.launch {
                    favouritesUseCases.deleteFavouriteUseCase(event.character)
                }
            }
        }
    }

    private suspend fun fetchAllCharacters() {
        val nextPage = page + 1
        println(">>>> fetchData, page=$nextPage ")
        getCharactersUseCase.invoke(nextPage)
            .onSuccess {
                println(">>>>>>>>>>>> response OK= $it")
                _state.value =
                    state.value.copy(
                        characters = state.value.characters + it,
                        isError = false
                    ) //todo set isMoreData
                page = nextPage
            }
            .onFailure {
                println(">>>>>>>>>>>> response NOK= ${it.message}")
                _state.value = state.value.copy(isError = true)
            }
    }

    private suspend fun fetchFavouritesCharacters() {
        println(">>>> fetchFavouritesCharacters")
        favouritesUseCases.getFavouritesUseCase()
            .onSuccess {
                println(">>>>>>>>>>>> response OK= $it")
                _state.value =
                    state.value.copy(
                        favourites = it,
                        isMoreData = false,
                        isError = false
                    ) //todo pagination
            }
            .onFailure {
                println(">>>>>>>>>>>> response NOK= ${it.message}")
                _state.value = state.value.copy(isError = true)
            }
    }

}