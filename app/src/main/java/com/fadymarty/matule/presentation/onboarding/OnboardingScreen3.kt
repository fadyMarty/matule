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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
import com.fadymarty.matule.common.ui.theme.SubTextLight
import com.fadymarty.matule.common.ui.theme.TextColor
import com.fadymarty.matule.presentation.components.MatuleButton
import com.fadymarty.matule.presentation.navigation.screen.Screen
import com.fadymarty.matule.presentation.onboarding.components.OnboardingIndicator

@Composable
fun OnboardingScreen3(
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
            )
    ) {
        Image(
            modifier = Modifier
                .size(77.dp)
                .offset(
                    x = 52.dp,
                    y = 108.dp
                )
                .alpha(0.7f),
            painter = painterResource(R.drawable.onboarding_misc),
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(R.drawable.onboarding_3),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )

            Spacer(
                modifier = Modifier
                    .height(60.dp)
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp),
                text = "У вас Есть Сила,\nЧтобы",
                fontFamily = RalewayFontFamily,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 44.2.sp,
                color = BlockColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp),
                text = "В вашей комнате много красивых и привлекательных растений",
                fontFamily = PoppinsFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                color = SubTextLight
            )

            Spacer(modifier = Modifier.height(40.dp))

            OnboardingIndicator(currentPage = 2)

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            MatuleButton(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                text = "Далее",
                onClick = {
                    navController.navigate(Screen.SignIn.route)
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
fun OnBoardingScreen3Preview() {
    MatuleTheme {
        OnboardingScreen3(rememberNavController())
    }
}