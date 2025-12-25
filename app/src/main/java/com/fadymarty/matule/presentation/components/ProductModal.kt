package com.fadymarty.matule.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fadymarty.matule_ui_kit.common.theme.MatuleTheme
import com.fadymarty.matule_ui_kit.presentation.components.buttons.BigButton
import com.fadymarty.matule_ui_kit.presentation.components.modal.Modal
import com.fadymarty.network.domain.model.Product

@Composable
fun ProductModal(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    product: Product,
    onClick: () -> Unit,
) {
    Modal(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = product.title,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Описание",
                style = MatuleTheme.typography.headlineMedium,
                color = MatuleTheme.colorScheme.placeholder
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = product.description,
                style = MatuleTheme.typography.textRegular
            )
            Spacer(Modifier.height(49.dp))
            Text(
                text = "Примерный расход",
                style = MatuleTheme.typography.captionSemibold,
                color = MatuleTheme.colorScheme.placeholder
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = product.approximateCost,
                style = MatuleTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(19.dp))
            BigButton(
                label = "Добавить за ${product.price} ₽",
                onClick = onClick
            )
            Spacer(Modifier.height(40.dp))
        }
    }
}