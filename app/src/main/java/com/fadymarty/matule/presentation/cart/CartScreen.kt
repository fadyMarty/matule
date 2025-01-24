package com.fadymarty.matule.presentation.cart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fadymarty.matule.R
import com.fadymarty.matule.common.ui.theme.AccentColor
import com.fadymarty.matule.common.ui.theme.BlockColor
import com.fadymarty.matule.common.ui.theme.MatuleTheme
import com.fadymarty.matule.common.ui.theme.PoppinsFontFamily
import com.fadymarty.matule.common.ui.theme.RalewayFontFamily
import com.fadymarty.matule.common.ui.theme.SubTextDark
import com.fadymarty.matule.common.ui.theme.TextColor
import com.fadymarty.matule.presentation.cart.components.CartItem
import com.fadymarty.matule.presentation.cart.components.CartListItem
import com.fadymarty.matule.presentation.components.BackButton
import com.fadymarty.matule.presentation.components.MatuleButton

@Composable
fun CartScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
            Alignment.Center
        ) {
            BackButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = {
                    navController.popBackStack()
                },
                containerColor = BlockColor,
                iconTint = TextColor
            )
            Text(
                text = "Корзина",
                color = TextColor,
                fontFamily = RalewayFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(start = 20.dp),
            text = "3 товара",
            color = TextColor,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                CartListItem(
                    onIncreaseClick = { },
                    onDecreaseClick = { },
                    onDeleteClick = { },
                    cartItem = CartItem(
                        name = "Nike Club Max",
                        price = 584.95f,
                        count = 1,
                        image = R.drawable.sneakers_image_1
                    )
                )
            }
            item {
                CartListItem(
                    onIncreaseClick = { },
                    onDecreaseClick = { },
                    onDeleteClick = { },
                    cartItem = CartItem(
                        name = "Nike Air Max 200",
                        price = 94.05f,
                        count = 1,
                        image = R.drawable.sneakers_image_1
                    )
                )
            }
            item {
                CartListItem(
                    onIncreaseClick = { },
                    onDecreaseClick = { },
                    onDeleteClick = { },
                    cartItem = CartItem(
                        name = "Nike Air Max 270 Essential",
                        price = 584.95f,
                        count = 1,
                        image = R.drawable.sneakers_image_1
                    )
                )
            }
        }
        Column(
            modifier = Modifier
                .background(BlockColor)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 34.dp,
                    bottom = 32.dp
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Сумма",
                    color = SubTextDark,
                    fontFamily = RalewayFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = "₽753.95",
                    color = TextColor,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Доставка",
                    color = SubTextDark,
                    fontFamily = RalewayFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = "₽60.20",
                    color = TextColor,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            val pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(12f, 12f),
                phase = 0f
            )

            Canvas(modifier = Modifier.fillMaxWidth()) {
                drawLine(
                    color = SubTextDark,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    pathEffect = pathEffect,
                    strokeWidth = 4f
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Итого",
                    color = SubTextDark,
                    fontFamily = RalewayFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                Text(
                    text = "₽814.15",
                    color = AccentColor,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            MatuleButton(
                onClick = { },
                text = "Оформить Заказ"
            )
        }
    }
}