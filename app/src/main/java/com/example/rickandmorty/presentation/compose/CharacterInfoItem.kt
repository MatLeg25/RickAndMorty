package com.example.rickandmorty.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.rickandmorty.R

@Composable
@Preview
fun CharacterInfoItem(
    modifier: Modifier = Modifier,
    avatarUrl: String = "",
    name: String = "Name eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee eeeeeeeeeeeeeeeeeeeeeee",
    isFavourite: Boolean = false,
    onFavoriteClick: () -> Unit = {}
) {
    val favouriteImage = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            imageVector = Icons.Default.AccountBox,
            contentDescription = stringResource(id = R.string.character_avatar)
        )
        Text(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Image(
            modifier = Modifier.clickable {
                onFavoriteClick()
            },
            imageVector = favouriteImage,
            contentDescription = stringResource(id = R.string.favourite)
        )
    }
}