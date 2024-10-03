package com.example.rickandmorty.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
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
    val contentDescription =
        stringResource(id = if (isFavourite) R.string.delete_favourite else R.string.add_favourite)
    val listColors =
        listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondaryContainer)
    val customBrush = remember {
        Brush.linearGradient(
            colors = listColors,
            tileMode = TileMode.Mirror
        )
    }

    Row(
        modifier = modifier
            .padding(8.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .background(customBrush),
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
                ),
            //todo update images
            placeholder = painterResource(R.drawable.ic_launcher_background),
            error = painterResource(R.drawable.ic_launcher_background)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            modifier = Modifier.testTag(stringResource(id = R.string.tag_add_delete_favourite)),
            onClick = { onFavoriteClick() }
        ) {
            Icon(
                imageVector = favouriteImage,
                contentDescription = contentDescription,
            )
        }
    }
}

@Composable
@Preview
private fun CharacterInfoItemPreview() = CharacterInfoItem(modifier = Modifier,
    avatarUrl = "",
    name = "Name eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee eeeeeeeeeeeeeeeeeeeeeee",
    isFavourite = false,
    onFavoriteClick = {})