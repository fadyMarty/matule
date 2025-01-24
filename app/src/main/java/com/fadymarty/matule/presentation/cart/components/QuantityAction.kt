package com.fadymarty.matule.presentation.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fadymarty.matule.R
import com.fadymarty.matule.common.ui.theme.AccentColor
import com.fadymarty.matule.common.ui.theme.BlockColor
import com.fadymarty.matule.common.ui.theme.MatuleTheme
import com.fadymarty.matule.common.ui.theme.RalewayFontFamily

@Composable
fun QuantityAction(
    modifier: Modifier = Modifier,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    count: Int = 1
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(AccentColor)
            .padding(
                vertical = 14.dp,
                horizontal = 22.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            modifier = Modifier
                .size(14.dp),
            onClick = {
                onIncreaseClick
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_increase),
                contentDescription = null,
                tint = BlockColor
            )
        }
        Text(
            text = count.toString(),
            fontFamily = RalewayFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = BlockColor
        )
        Icon(
            modifier = Modifier
                .width(14.dp)
                .clickable {
                    onDecreaseClick()
                },
            painter = painterResource(R.drawable.ic_decrease),
            contentDescription = null,
            tint = BlockColor
        )
    }
}

@Preview
@Composable
fun QuantityActionPreview() {
    MatuleTheme {
        QuantityAction(
            onIncreaseClick = { },
            onDecreaseClick = { }
        )
    }
}