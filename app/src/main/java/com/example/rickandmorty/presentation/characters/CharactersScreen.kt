package com.example.rickandmorty.presentation.characters

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rickandmorty.R
import com.example.rickandmorty.presentation.compose.ChangeScreenModeBtn
import com.example.rickandmorty.presentation.compose.CharacterInfoItem
import com.example.rickandmorty.presentation.compose.LoadMore

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CharactersScreen(
    modifier: Modifier = Modifier,
    viewModel: CharactersViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state = viewModel.state.value
    val items = if (state.screenMode == ScreenMode.ALL) state.characters else state.favourites
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { viewModel.onEvent(CharactersScreenEvent.Refresh) }
    )

    //todo update UI to inform user about error
    if (state.isError) {
        Toast.makeText(context, R.string.cannot_fetch_resource_try_again_later, Toast.LENGTH_LONG)
            .show()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ChangeScreenModeBtn(
                modifier = Modifier,
                btnTextResId = R.string.all,
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
                btnTextResId = R.string.favourite,
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

        //todo fix PullRefresh bug for empty screen
        PullRefreshIndicator(
            refreshing = state.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            backgroundColor = if (state.isRefreshing) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
        )

        LazyColumn(modifier = Modifier.testTag(stringResource(id = R.string.tag_lazy_list))) {
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
                    loadMoreFun = { viewModel.onEvent(CharactersScreenEvent.FetchNextPage) }
                )
            }
        }

    }


}