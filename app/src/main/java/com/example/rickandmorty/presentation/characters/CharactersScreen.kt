package com.example.rickandmorty.presentation.characters

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rickandmorty.Greeting
import com.example.rickandmorty.presentation.compose.CharacterInfoItem
import com.example.rickandmorty.presentation.compose.LoadMore

@Composable
fun CharactersScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    viewModel: CharactersViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    LazyColumn(modifier = modifier.padding(innerPadding)) {
        items(state.characters) {
            CharacterInfoItem(
                modifier = Modifier.fillMaxWidth(),
                name = it.name,
                avatarUrl = it.avatarUrl
            )
        }
        item { //todo validate pagination
            LoadMore(
                isMoreData = state.isMoreData,
                loadMoreFun = viewModel::fetchData
            )
        }
    }
}