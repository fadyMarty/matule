package com.fadymarty.matule.presentation.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.fadymarty.matule.common.ui.theme.BlockColor
import com.fadymarty.matule.common.ui.theme.DisableColor

@Composable
fun OnboardingIndicator(
    modifier: Modifier = Modifier,
    currentPage: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(3) { page ->
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .clip(CircleShape)
                    .width(
                        if (currentPage == page) 43.dp
                        else 28.dp
                    )
                    .background(
                        if (currentPage == page) BlockColor
                        else DisableColor
                    )
            )
        }
    }
}