package com.showedup.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.showedup.app.ui.theme.ChipShape

@Composable
fun PillChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) selectedColor.copy(alpha = 0.15f) else unselectedColor,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "chipBg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "chipContent"
    )

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = ChipShape,
        color = backgroundColor,
        border = if (selected) BorderStroke(1.dp, selectedColor.copy(alpha = 0.3f)) else null
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = contentColor
        )
    }
}
