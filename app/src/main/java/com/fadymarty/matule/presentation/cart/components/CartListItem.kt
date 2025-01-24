package com.fadymarty.matule.presentation.cart.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fadymarty.matule.R
import com.fadymarty.matule.common.ui.theme.BackgroundColor
import com.fadymarty.matule.common.ui.theme.BlockColor
import com.fadymarty.matule.common.ui.theme.MatuleTheme
import com.fadymarty.matule.common.ui.theme.PoppinsFontFamily
import com.fadymarty.matule.common.ui.theme.RalewayFontFamily
import com.fadymarty.matule.common.ui.theme.TextColor
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CartListItem(
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    cartItem: CartItem
) {
    val density = LocalDensity.current

    val defaultActionSize = 68.dp
    val actionSizePx = with(density) { defaultActionSize.toPx() }
    val endActionSizePx = with(density) { defaultActionSize.toPx() }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Center,
            anchors = DraggableAnchors {
                DragAnchors.Start at -actionSizePx
                DragAnchors.Center at 0f
                DragAnchors.End at endActionSizePx
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
        )
    }

    DraggableItem(
        state = state,
        startAction = {
            QuantityAction(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(defaultActionSize)
                    .fillMaxHeight()
                    .padding(end = 10.dp)
                    .offset {
                        IntOffset(
                            ((-state
                                .requireOffset() - actionSizePx))
                                .roundToInt(), 0
                        )
                    },
                onIncreaseClick = {
                    onIncreaseClick()
                },
                onDecreaseClick = {
                    onDecreaseClick()
                }
            )
        },
        endAction = {
            Spacer(modifier = Modifier.width(10.dp))
            DeleteAction(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(defaultActionSize)
                    .fillMaxHeight()
                    .padding(start = 10.dp)
                    .offset {
                        IntOffset(
                            ((-state
                                .requireOffset()) + endActionSizePx)
                                .roundToInt(), 0
                        )
                    },
                onClick = {
                    onDeleteClick()
                }
            )
        },
        content = {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .height(104.dp)
                    .fillMaxWidth()
                    .background(BlockColor)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .width(87.dp)
                        .height(85.dp)
                        .background(BackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.sneakers_image_1),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(30.dp))

                Column {
                    Text(
                        text = cartItem.name,
                        fontFamily = RalewayFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = TextColor
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "₽" + cartItem.price,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = TextColor
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun CartItemPreview() {
    MatuleTheme {
        CartListItem(
            onIncreaseClick = { },
            onDecreaseClick = { },
            onDeleteClick = { },
            cartItem = CartItem(
                name = "Nike Air Max 270 Essential",
                price = 74.95f,
                count = 1,
                image = R.drawable.sneakers_image_1
            )
        )
    }
}