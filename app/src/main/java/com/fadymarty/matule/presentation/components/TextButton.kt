package com.fadymarty.matule.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fadymarty.matule.common.ui.theme.RalewayFontFamily

@Composable
fun MatuleButton(
    text: String,
    onClick: () -> Unit,
    @DrawableRes icon: Int? = null,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    iconTint: Color = Color.Unspecified,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (icon != null) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart),
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = iconTint
                )
            }
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = text,
                fontSize = 14.sp,
                fontFamily = RalewayFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}