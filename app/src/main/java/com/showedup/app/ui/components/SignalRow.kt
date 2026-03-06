package com.showedup.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.showedup.app.ui.theme.*

data class SignalState(
    val gps: Boolean = false,
    val wifi: Boolean = false,
    val bluetooth: Boolean = false,
    val audio: Boolean = false,
    val sensor: Boolean = false
)

private val signalColors = listOf(SignalGps, SignalWifi, SignalBluetooth, SignalAudio, SignalSensor)

@Composable
fun SignalRow(
    state: SignalState,
    modifier: Modifier = Modifier,
    dotSize: Dp = 10.dp,
    spacing: Dp = 8.dp
) {
    val states = listOf(state.gps, state.wifi, state.bluetooth, state.audio, state.sensor)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        states.forEachIndexed { index, active ->
            SignalDot(
                active = active,
                color = signalColors[index],
                size = dotSize,
                delayMs = index * 120
            )
        }
    }
}

@Composable
private fun SignalDot(
    active: Boolean,
    color: Color,
    size: Dp,
    delayMs: Int
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val scale by if (active) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, delayMillis = delayMs, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "dotPulse"
        )
    } else {
        remember { mutableFloatStateOf(1f) }
    }

    Box(
        modifier = Modifier
            .size(size)
            .scale(scale)
            .clip(CircleShape)
            .background(
                if (active) color
                else color.copy(alpha = 0.2f)
            )
    )
}

@Composable
fun AnimatedDots(
    modifier: Modifier = Modifier,
    dotSize: Dp = 8.dp,
    spacing: Dp = 6.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loadingDots")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(400, delayMillis = index * 100, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot$index"
            )
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(Emerald500.copy(alpha = alpha))
            )
        }
    }
}
