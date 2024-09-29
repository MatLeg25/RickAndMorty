package com.example.rickandmorty.presentation.characters

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rickandmorty.Greeting

@Composable
fun CharactersScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    viewModel: CharactersViewModel = hiltViewModel()
) {

    viewModel.fetchData()

    Greeting(
        name = "Android",
        modifier = Modifier.padding(innerPadding)
    )
}