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

    init {
        fetchData()
    }

    fun fetchData() {
        println(">>>> fetchData ")
        viewModelScope.launch {
            getCharactersUseCase.invoke(1)
                .onSuccess {
                    println(">>>>>>>>>>>> response OK= $it")
                    _state.value = state.value.copy(characters = it) //todo set isMoreData
                }
                .onFailure {
                    println(">>>>>>>>>>>> response NOK= ${it.message}")
                }

        }
    }

    fun onEvent() {

    }

}