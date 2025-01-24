package com.fadymarty.matule.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fadymarty.matule.R
import com.fadymarty.matule.common.ui.theme.AccentColor
import com.fadymarty.matule.common.ui.theme.BlockColor
import com.fadymarty.matule.common.ui.theme.MatuleTheme
import com.fadymarty.matule.common.ui.theme.RalewayFontFamily
import com.fadymarty.matule.common.ui.theme.TextColor
import com.fadymarty.matule.presentation.components.MatuleButton
import com.fadymarty.matule.presentation.navigation.screen.Screen
import com.fadymarty.matule.presentation.onboarding.components.OnboardingIndicator

@Composable
fun OnboardingScreen1(
    navController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        AccentColor,
                        Color(0xFF0076B1)
                    )
                )
            ),
    ) {
        Image(
            modifier = Modifier
                .offset(
                    x = 47.dp,
                    y = 83.dp
                ),
            painter = painterResource(R.drawable.ic_highlight_2),
            contentDescription = null
        )
        Image(
            modifier = Modifier
                .size(45.dp)
                .offset(
                    x = 45.dp,
                    y = 317.dp
                ),
            painter = painterResource(R.drawable.onboarding_misc),
            contentDescription = null
        )
        Image(
            modifier = Modifier
                .width(90.dp)
                .height(54.dp)
                .offset(
                    x = 242.dp,
                    y = 546.dp
                )
                .rotate(-42.25f),
            painter = painterResource(R.drawable.ic_highlight_1),
            contentDescription = null
        )
        Image(
            modifier = Modifier
                .width(90.dp)
                .height(54.dp)
                .offset(
                    x = 29.dp,
                    y = 613.dp
                )
                .rotate(68.98f),
            painter = painterResource(R.drawable.ic_highlight_1),
            contentDescription = null
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(70.dp))

            Text(
                text = "ДОБРО\nПОЖАЛОВАТЬ",
                fontFamily = RalewayFontFamily,
                fontWeight = FontWeight.Black,
                color = BlockColor,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                lineHeight = 35.22.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(R.drawable.onboarding_vector),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(80.dp))

            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.onboarding_1),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )

            Spacer(
                modifier = Modifier
                    .height(40.dp)
            )

            OnboardingIndicator(
                currentPage = 0
            )

            Spacer(modifier = Modifier.weight(1f))

            MatuleButton(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                text = "Начать",
                onClick = {
                    navController.navigate(Screen.Onboarding2.route)
                },
                contentColor = TextColor,
                containerColor = BlockColor
            )

            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(36.dp)
            )
        }
    }
}

@Preview
@Composable
fun OnBoardingScreen1Preview() {
    MatuleTheme {
        OnboardingScreen1(rememberNavController())
    }
}