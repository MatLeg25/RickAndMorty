package com.example.rickandmorty.presentation.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rickandmorty.R
import com.example.rickandmorty.presentation.compose.ChangeScreenModeBtn
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
                ChangeScreenModeBtn(
                    modifier = Modifier,
                    btnTextResId =  R.string.all,
                    isSelected = state.screenMode == ScreenMode.ALL,
                    onClick = {
                        viewModel.onEvent(
                            CharactersScreenEvent.ChangeScreenMode(
                                ScreenMode.ALL
                            )
                        )
                    }
                )
                ChangeScreenModeBtn(
                    modifier = Modifier,
                    btnTextResId =  R.string.favourite,
                    isSelected = state.screenMode == ScreenMode.FAVOURITES,
                    onClick = {
                        viewModel.onEvent(
                            CharactersScreenEvent.ChangeScreenMode(
                                ScreenMode.FAVOURITES
                            )
                        )
                    }
                )
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