package com.showedup.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.showedup.app.ui.theme.*

enum class ButtonVariant { PRIMARY, GHOST, OUTLINED }

@Composable
fun ShowedUpButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.PRIMARY,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: @Composable (() -> Unit)? = null
) {
    when (variant) {
        ButtonVariant.PRIMARY -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(52.dp),
                enabled = enabled && !loading,
                shape = PillShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Emerald500,
                    contentColor = Gray950,
                    disabledContainerColor = Emerald500.copy(alpha = 0.4f),
                    disabledContentColor = Gray950.copy(alpha = 0.6f)
                )
            ) {
                ButtonContent(text, loading, icon)
            }
        }
        ButtonVariant.GHOST -> {
            TextButton(
                onClick = onClick,
                modifier = modifier.height(52.dp),
                enabled = enabled && !loading,
                shape = PillShape
            ) {
                ButtonContent(
                    text = text,
                    loading = loading,
                    icon = icon,
                    textColor = Emerald500
                )
            }
        }
        ButtonVariant.OUTLINED -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.height(52.dp),
                enabled = enabled && !loading,
                shape = PillShape,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Emerald500
                )
            ) {
                ButtonContent(
                    text = text,
                    loading = loading,
                    icon = icon,
                    textColor = Emerald500
                )
            }
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    loading: Boolean,
    icon: @Composable (() -> Unit)? = null,
    textColor: Color = Color.Unspecified
) {
    if (loading) {
        AnimatedDots(dotSize = 6.dp, spacing = 4.dp)
    } else {
        if (icon != null) {
            icon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}
