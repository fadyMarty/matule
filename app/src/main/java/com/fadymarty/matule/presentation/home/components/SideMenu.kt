package com.fadymarty.matule.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.fadymarty.matule.common.util.NavigationItem

val sideMenuItems = listOf(
    NavigationItem(
        icon = R.drawable.ic_settings,
        label = "Профиль"
    ),
    NavigationItem(
        icon = R.drawable.ic_settings,
        label = "Корзина"
    ),
    NavigationItem(
        icon = R.drawable.ic_settings,
        label = "Избранное"
    ),
    NavigationItem(
        icon = R.drawable.ic_settings,
        label = "Заказы"
    ),
    NavigationItem(
        icon = R.drawable.ic_settings,
        label = "Уведомления"
    ),
    NavigationItem(
        icon = R.drawable.ic_settings,
        label = "Настройки"
    )
)

@Composable
fun SideMenu() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(AccentColor)
                    .statusBarsPadding()
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Image(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .padding(start = 36.dp),
                    painter = painterResource(R.drawable.photo),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    modifier = Modifier
                        .padding(start = 35.dp),
                    text = "Эмануэль Кверти",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = RalewayFontFamily,
                    color = BlockColor
                )

                Spacer(
                    modifier = Modifier.height(55.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(start = 20.dp)
                ) {
                    sideMenuItems.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .clickable { }
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(item.icon),
                                contentDescription = null,
                                tint = BlockColor
                            )

                            Spacer(modifier = Modifier.width(25.dp))

                            Text(
                                text = item.label,
                                fontWeight = FontWeight.Medium,
                                fontFamily = RalewayFontFamily,
                                fontSize = 16.sp,
                                color = BlockColor
                            )
                        }
                        Spacer(
                            modifier = Modifier
                                .height(
                                    if (index == sideMenuItems.size - 1) {
                                        0.dp
                                    } else {
                                        30.dp
                                    }
                                )
                        )
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(25.dp).fillMaxSize().background(Color.Red)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SideMenuPreview() {
    MatuleTheme {
        SideMenu()
    }
}