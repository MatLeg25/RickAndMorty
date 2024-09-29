package com.example.rickandmorty.presentation.characters

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.useCase.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private var _state = mutableStateOf(CharactersScreenState())
    val state: State<CharactersScreenState> = _state

    private var page: Int = 0

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            val nextPage = page + 1
            println(">>>> fetchData, page=$nextPage ")
            getCharactersUseCase.invoke(nextPage)
                .onSuccess {
                    println(">>>>>>>>>>>> response OK= $it")
                    _state.value =
                        state.value.copy(characters = state.value.characters + it) //todo set isMoreData
                    page = nextPage
                }
                .onFailure {
                    println(">>>>>>>>>>>> response NOK= ${it.message}")
                }

        }
    }

    fun onEvent(event: CharactersScreenEvent) {
        when (event) {
            is CharactersScreenEvent.Refresh -> {}
            is CharactersScreenEvent.FetchNextPage -> {}
            is CharactersScreenEvent.ChangeScreenMode -> {
                if (event.mode != state.value.screenMode) {
                    _state.value = state.value.copy(screenMode = event.mode)
                }
            }
        }
    }

}