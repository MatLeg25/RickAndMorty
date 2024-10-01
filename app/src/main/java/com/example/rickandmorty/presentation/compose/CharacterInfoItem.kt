package com.example.rickandmorty.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rickandmorty.R

@Composable
fun CharacterInfoItem(
    modifier: Modifier = Modifier,
    avatarUrl: String,
    name: String,
    isFavourite: Boolean,
    onFavoriteClick: () -> Unit
) {
    val favouriteImage = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = stringResource(id = R.string.character_avatar),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(72.dp)
                .background(Color.Gray)
                .clip(
                    RoundedCornerShape(
                        topStart = 10.dp,
                        bottomStart = 10.dp,
                    )
                )
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

@Composable
@Preview
private fun CharacterInfoItemPreview() = CharacterInfoItem(modifier = Modifier,
    avatarUrl = "",
    name = "Name eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee eeeeeeeeeeeeeeeeeeeeeee",
    isFavourite = false,
    onFavoriteClick = {})