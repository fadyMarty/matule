package com.fadymarty.matule.presentation.authentication.sign_up

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fadymarty.matule.R
import com.fadymarty.matule.common.ui.theme.BackgroundColor
import com.fadymarty.matule.common.ui.theme.BlockColor
import com.fadymarty.matule.common.ui.theme.HintColor
import com.fadymarty.matule.common.ui.theme.PoppinsFontFamily
import com.fadymarty.matule.common.ui.theme.RalewayFontFamily
import com.fadymarty.matule.common.ui.theme.SubTextDark
import com.fadymarty.matule.common.ui.theme.TextColor
import com.fadymarty.matule.presentation.components.BackButton
import com.fadymarty.matule.presentation.components.MatuleButton
import com.fadymarty.matule.presentation.components.MatuleDialog
import com.fadymarty.matule.presentation.components.TextEntryModule
import com.fadymarty.matule.presentation.navigation.graph.Graph
import com.fadymarty.matule.presentation.navigation.screen.Screen

@Composable
fun SignUpScreen(
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState().value
    val isDialogShown = viewModel.isDialogShown.collectAsState().value
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is SignUpViewModel.ValidationEvent.Success -> {
                    navController.navigate(Graph.MAIN) {
                        popUpTo(navController.graph.id)
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlockColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(
                    start = 20.dp,
                    top = 66.dp,
                    end = 20.dp,
                    bottom = 50.dp
                )
                .blur(
                    if (isDialogShown) {
                        4.dp
                    } else {
                        0.dp
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButton(
                modifier = Modifier.align(Alignment.Start),
                onClick = {
                    navController.popBackStack()
                },
                containerColor = BackgroundColor,
                iconTint = TextColor
            )

            Spacer(modifier = Modifier.height(11.dp))

            Text(
                text = "Привет!",
                fontSize = 32.sp,
                fontFamily = RalewayFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = "Заполните Свои Данные Или Продолжите Через Cоциальные Mедиа",
                color = SubTextDark,
                fontSize = 16.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            TextEntryModule(
                description = "Имя",
                textFieldValue = state.name,
                onValueChange = {
                    viewModel.onEvent(SignUpEvent.NameChanged(it))
                },
                placeholder = {
                    Text(
                        text = "xxxxxxxx",
                        color = HintColor,
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextEntryModule(
                description = "Email",
                textFieldValue = state.email,
                onValueChange = {
                    viewModel.onEvent(SignUpEvent.EmailChanged(it))
                },
                placeholder = {
                    Text(
                        text = "xyz@gamil.com",
                        color = HintColor,
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            TextEntryModule(
                description = "Пароль",
                textFieldValue = state.password,
                onValueChange = {
                    viewModel.onEvent(SignUpEvent.PasswordChanged(it))
                },
                placeholder = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        repeat(8) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(6.dp)
                                    .background(HintColor)
                            )
                        }
                    }
                },
                trailingIcon1 = R.drawable.ic_eye_open,
                trailingIcon2 = R.drawable.ic_eye_close,
                onTrailingIconClick = {
                    viewModel.onEvent(
                        SignUpEvent.ToggleVisualTransformation(state.isPasswordShown)
                    )
                },
                visualTransformation = if (state.isPasswordShown) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                isPasswordShown = state.isPasswordShown,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.onEvent(SignUpEvent.SignUp)
                    }
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(BackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_policy_check),
                        contentDescription = null,
                        tint = TextColor,
                        modifier = Modifier.size(10.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    modifier = Modifier
                        .clickable {

                        },
                    text = "Даю согласие на обработку персональных данных",
                    fontFamily = RalewayFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = HintColor,
                    textDecoration = TextDecoration.Underline
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            MatuleButton(
                text = "Зарегистрироваться",
                onClick = {
                    viewModel.onEvent(SignUpEvent.SignUp)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row {
                Text(
                    modifier = Modifier
                        .clickable {
                            navController.popBackStack()
                            navController.navigate(Screen.SignIn.route) {
                                launchSingleTop = true
                            }
                        },
                    text = buildAnnotatedString {
                        append("Есть аккаунт? ")
                        withStyle(
                            style = SpanStyle(
                                color = TextColor
                            )
                        ) {
                            append("Войти")
                        }
                    },
                    fontFamily = RalewayFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = HintColor,
                    fontSize = 16.sp
                )
            }
        }

        if (isDialogShown) {
            MatuleDialog(
                onDismiss = {
                    viewModel.onDismissDialog()
                },
                icon = R.drawable.onboarding_misc,
                "xxxxxx",
                "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
            )
        }
    }
}