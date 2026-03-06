package com.showedup.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.showedup.app.ui.theme.*

enum class AttendanceStatus(val label: String, val color: Color) {
    PRESENT("Present", StatusPresent),
    ABSENT("Absent", StatusAbsent),
    DAY_OFF("Day Off", StatusDayOff),
    HOLIDAY("Holiday", StatusHoliday),
    UPCOMING("Upcoming", StatusUpcoming)
}

@Composable
fun StatusBadge(
    status: AttendanceStatus,
    modifier: Modifier = Modifier
) {
    val animatedColor by animateColorAsState(
        targetValue = status.color,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "statusColor"
    )

    Box(
        modifier = modifier
            .clip(PillShape)
            .background(animatedColor.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = status.label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = animatedColor
        )
    }
}
