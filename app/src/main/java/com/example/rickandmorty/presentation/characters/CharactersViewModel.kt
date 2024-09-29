package com.example.rickandmorty.presentation.characters

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

    fun fetchData() {
        println(">>>> fetchData ")
        viewModelScope.launch {
            getCharactersUseCase.invoke(1)
                .onSuccess {
                    println(">>>>>>>>>>>> response OK= $it")
                }
                .onFailure {
                    println(">>>>>>>>>>>> response NOK= ${it.message}")
                }

        }
    }

}