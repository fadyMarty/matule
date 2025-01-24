package com.fadymarty.matule.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fadymarty.matule.R
import com.fadymarty.matule.common.ui.theme.BackgroundColor
import com.fadymarty.matule.common.ui.theme.MatuleTheme
import com.fadymarty.matule.common.ui.theme.TextColor

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Color,
    iconTint: Color
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(44.dp)
    ) {
        IconButton(
            modifier = Modifier
                .fillMaxSize()
                .background(containerColor)
                .clip(CircleShape),
            onClick = { onClick() }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_back),
                contentDescription = null,
                tint = iconTint
            )
        }
    }
}