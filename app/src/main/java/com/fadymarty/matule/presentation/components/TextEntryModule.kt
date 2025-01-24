package com.fadymarty.matule.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fadymarty.matule.R
import com.fadymarty.matule.common.ui.theme.BackgroundColor
import com.fadymarty.matule.common.ui.theme.HintColor
import com.fadymarty.matule.common.ui.theme.PoppinsFontFamily
import com.fadymarty.matule.common.ui.theme.RalewayFontFamily
import com.fadymarty.matule.common.ui.theme.TextColor

@Composable
fun TextEntryModule(
    modifier: Modifier = Modifier,
    description: String,
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable () -> Unit,
    @DrawableRes trailingIcon1: Int? = null,
    @DrawableRes trailingIcon2: Int? = null,
    isPasswordShown: Boolean = false,
    onTrailingIconClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    Column(modifier = modifier) {
        Text(
            text = description,
            fontSize = 16.sp,
            fontFamily = RalewayFontFamily,
            fontWeight = FontWeight.Medium
        )
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue,
            onValueChange = {
                onValueChange(it)
            },
            shape = RoundedCornerShape(14.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BackgroundColor,
                unfocusedContainerColor = BackgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = TextColor,
                unfocusedTextColor = TextColor
            ),
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium
            ),
            placeholder = {
                placeholder()
            },
            singleLine = true,
            trailingIcon = {
                if (trailingIcon1 != null && trailingIcon2 != null) {
                    IconButton(
                        onClick = {
                            if (onTrailingIconClick != null) {
                                onTrailingIconClick()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isPasswordShown) {
                                    R.drawable.ic_eye_open
                                } else {
                                    R.drawable.ic_eye_close
                                }
                            ),
                            contentDescription = null,
                            tint = HintColor
                        )
                    }
                }
            },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions
        )
    }
}