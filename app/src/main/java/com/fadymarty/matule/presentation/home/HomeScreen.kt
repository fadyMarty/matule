package com.fadymarty.matule.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fadymarty.matule.R
import com.fadymarty.matule.common.ui.theme.BlockColor
import com.fadymarty.matule.common.ui.theme.MatuleTheme
import com.fadymarty.matule.common.ui.theme.TextColor
import com.fadymarty.matule.presentation.navigation.screen.Screen

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .clickable {

                        },
                    painter = painterResource(R.drawable.ic_hamburger),
                    contentDescription = null
                )
                Text(
                    text = "Главная",
                    color = TextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
                IconButton(
                    modifier = Modifier
                        .clip(RoundedCornerShape(40.dp))
                        .background(BlockColor)
                        .size(44.dp),
                    onClick = {
                        navController.navigate(Screen.Cart.route)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        tint = TextColor
                    )
                }
            }
        }
        Spacer(
            modifier = Modifier.height(19.dp)
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    MatuleTheme {
        HomeScreen(rememberNavController())
    }
}