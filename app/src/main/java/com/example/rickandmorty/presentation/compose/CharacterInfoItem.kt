package com.example.rickandmorty.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun CharacterInfoItem(
    modifier: Modifier = Modifier,
    avatarUrl: String = "",
    name: String = "Name"
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(imageVector = Icons.Default.AccountBox, contentDescription = "desc")
        Text(text = name)
    }
}