package com.showedup.app.ui.screens.signal

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.showedup.app.ui.components.AnimatedDots
import com.showedup.app.ui.components.SignalRow
import com.showedup.app.ui.components.SignalState
import com.showedup.app.ui.theme.*

/**
 * Signal Collection Overlay — floating pill shown during attendance recording.
 * This is not a full-screen composable but an overlay that can be placed
 * anywhere in the composition.
 */
enum class CollectionState {
    IDLE, COLLECTING, SUCCESS, FAILED
}

@Composable
fun SignalCollectionOverlay(
    state: CollectionState,
    signalState: SignalState = SignalState(),
    courseName: String = "",
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state != CollectionState.IDLE,
        enter = slideInVertically(initialOffsetY = { it }) +
                fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(targetOffsetY = { it }) +
                fadeOut(animationSpec = tween(200)),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.screenHorizontal)
                .shadow(
                    elevation = 8.dp,
                    shape = PillShape,
                    ambientColor = Emerald500.copy(alpha = 0.2f)
                ),
            shape = PillShape,
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                when (state) {
                    CollectionState.COLLECTING -> Emerald500.copy(alpha = 0.3f)
                    CollectionState.SUCCESS -> Emerald500.copy(alpha = 0.5f)
                    CollectionState.FAILED -> ErrorRed.copy(alpha = 0.3f)
                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                }
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.lg, vertical = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status indicator
                when (state) {
                    CollectionState.COLLECTING -> {
                        AnimatedDots(dotSize = 6.dp, spacing = 3.dp)
                    }
                    CollectionState.SUCCESS -> {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Emerald500),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Gray950
                            )
                        }
                    }
                    CollectionState.FAILED -> {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(ErrorRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = White
                            )
                        }
                    }
                    else -> {}
                }

                Spacer(modifier = Modifier.width(Spacing.sm))

                // Text
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = when (state) {
                            CollectionState.COLLECTING -> "Recording attendance…"
                            CollectionState.SUCCESS -> "Done!"
                            CollectionState.FAILED -> "Could not record"
                            else -> ""
                        },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (courseName.isNotBlank() && state == CollectionState.COLLECTING) {
                        Text(
                            text = courseName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Signal dots (visible during collection)
                if (state == CollectionState.COLLECTING || state == CollectionState.SUCCESS) {
                    SignalRow(
                        state = signalState,
                        dotSize = 8.dp,
                        spacing = 4.dp
                    )
                }
            }
        }
    }
}
