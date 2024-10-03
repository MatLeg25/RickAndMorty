package com.example.rickandmorty.presentation.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.R

@Composable
fun ChangeScreenModeBtn(
    modifier: Modifier = Modifier,
    @StringRes btnTextResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = if (isSelected) modifier
            .border(4.dp, MaterialTheme.colorScheme.primaryContainer, RectangleShape)
            .testTag(stringResource(id = R.string.tag_has_border)) else modifier,
        onClick = {
            onClick()
        },
        shape = RectangleShape
    ) {
        Text(
            text = stringResource(id = btnTextResId),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
@Preview
fun ChangeScreenModeBtnPreview(
    modifier: Modifier = Modifier,
    @StringRes btnTextResId: Int = R.string.favourite,
    isSelected: Boolean = true,
    onClick: () -> Unit = {}
) {
    ChangeScreenModeBtn(modifier, btnTextResId, isSelected, onClick)
}