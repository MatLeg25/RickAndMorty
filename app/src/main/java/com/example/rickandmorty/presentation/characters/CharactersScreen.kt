package com.example.rickandmorty.presentation.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rickandmorty.Greeting
import com.example.rickandmorty.R
import com.example.rickandmorty.presentation.compose.CharacterInfoItem
import com.example.rickandmorty.presentation.compose.LoadMore

@Composable
fun CharactersScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    viewModel: CharactersViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val items = if (state.screenMode == ScreenMode.ALL) state.characters else state.favourites

    LazyColumn(modifier = modifier.padding(innerPadding)) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = {
                    viewModel.onEvent(
                        CharactersScreenEvent.ChangeScreenMode(
                            ScreenMode.ALL
                        )
                    )
                }) {
                    Text(text = stringResource(id = R.string.all))
                }
                Button(onClick = {
                    viewModel.onEvent(
                        CharactersScreenEvent.ChangeScreenMode(
                            ScreenMode.FAVOURITES
                        )
                    )
                }) {
                    Text(text = stringResource(id = R.string.favourite))
                }
            }
        }
        items(items) {
            CharacterInfoItem(
                modifier = Modifier.fillMaxWidth(),
                name = it.name,
                avatarUrl = it.avatarUrl,
                isFavourite = it.isFavourite,
                onFavoriteClick = {
                    viewModel.onEvent(CharactersScreenEvent.AddDeleteFavourite(it))
                }
            )
        }
        item {
            LoadMore(
                isMoreData = state.isMoreData,
                loadMoreFun = viewModel::fetchData
            )
        }
    }
}