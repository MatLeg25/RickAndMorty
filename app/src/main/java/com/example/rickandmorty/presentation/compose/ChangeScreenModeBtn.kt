package com.example.rickandmorty.presentation.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.R

@Composable
fun ChangeScreenModeBtn(
    modifier: Modifier = Modifier,
    @StringRes btnTextResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
//todo update style
    Button(
        modifier = if (isSelected) modifier
            .border(
                4.dp,
                MaterialTheme.colorScheme.secondary,
                RoundedCornerShape(percent = 50)
            )
            .testTag(stringResource(id = R.string.tag_has_border)) else modifier,
        onClick = {
            onClick()
        }) {
        Text(text = stringResource(id = btnTextResId))
    }
}