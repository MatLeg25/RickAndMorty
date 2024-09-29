package com.example.rickandmorty.presentation.characters

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(): ViewModel() {

    fun fetchData() {
        println(">>>> fetchData ")
    }

}