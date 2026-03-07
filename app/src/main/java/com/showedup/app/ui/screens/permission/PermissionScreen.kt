package com.showedup.app.ui.screens.permission

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.showedup.app.ui.components.ButtonVariant
import com.showedup.app.ui.components.ShowedUpButton
import com.showedup.app.ui.theme.*

private data class PermissionItem(
    val icon: ImageVector,
    val label: String,
    val color: androidx.compose.ui.graphics.Color
)

private val permissionItems = listOf(
    PermissionItem(Icons.Default.LocationOn, "Location — confirms you're in the classroom", SignalGps),
    PermissionItem(Icons.Default.Mic, "Microphone — captures an audio fingerprint", SignalAudio),
    PermissionItem(Icons.Default.Bluetooth, "Bluetooth — detects nearby devices", SignalBluetooth),
    PermissionItem(Icons.Default.Notifications, "Notifications — tells you when recording", Violet400)
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    onComplete: () -> Unit
) {
    val context = LocalContext.current

    val requiredPermissions = buildList {
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        add(Manifest.permission.ACCESS_COARSE_LOCATION)
        add(Manifest.permission.RECORD_AUDIO)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(Manifest.permission.BLUETOOTH_SCAN)
            add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val permissionState = rememberMultiplePermissionsState(requiredPermissions)

    // Track if we've already requested once
    var hasRequested by remember { mutableStateOf(false) }

    // Auto-proceed if all granted
    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted && hasRequested) {
            onComplete()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Gray950, Gray900)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = Spacing.screenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.15f))

            // Icon
            Surface(
                modifier = Modifier.size(96.dp),
                shape = CircleShape,
                color = Emerald500.copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(
                    2.dp, Emerald500.copy(alpha = 0.3f)
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = Emerald500
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.xxl))

            Text(
                text = "ShowedUp needs\na few permissions",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            Text(
                text = "These let the app record your attendance automatically.\nYour data never leaves your device.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.xxl))

            // Permission items list
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                permissionItems.forEachIndexed { index, item ->
                    val isGranted = when (index) {
                        0 -> permissionState.permissions.any {
                            it.permission == Manifest.permission.ACCESS_FINE_LOCATION && it.status.isGranted
                        }
                        1 -> permissionState.permissions.any {
                            it.permission == Manifest.permission.RECORD_AUDIO && it.status.isGranted
                        }
                        2 -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            permissionState.permissions.any {
                                it.permission == Manifest.permission.BLUETOOTH_SCAN && it.status.isGranted
                            }
                        } else true
                        3 -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionState.permissions.any {
                                it.permission == Manifest.permission.POST_NOTIFICATIONS && it.status.isGranted
                            }
                        } else true
                        else -> false
                    }

                    PermissionRow(
                        item = item,
                        isGranted = isGranted && hasRequested
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.15f))

            // Status message after requesting
            if (hasRequested && !permissionState.allPermissionsGranted) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Spacing.md),
                    shape = RoundedCornerShape(12.dp),
                    color = WarningAmber.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, WarningAmber.copy(alpha = 0.2f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(Spacing.sm),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = WarningAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(Spacing.xs))
                        Text(
                            text = "Some signals won't be available — attendance proof will be weaker",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarningAmber
                        )
                    }
                }
            }

            if (hasRequested && permissionState.allPermissionsGranted) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Spacing.md),
                    shape = RoundedCornerShape(12.dp),
                    color = Emerald500.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(Spacing.sm),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Emerald500,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(Spacing.xs))
                        Text(
                            text = "All permissions granted!",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Emerald500
                        )
                    }
                }
            }

            // Buttons
            if (!hasRequested) {
                ShowedUpButton(
                    text = "Grant Permissions",
                    onClick = {
                        hasRequested = true
                        permissionState.launchMultiplePermissionRequest()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            } else if (permissionState.allPermissionsGranted) {
                ShowedUpButton(
                    text = "Continue",
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth(),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            } else {
                // Some denied — show two options
                ShowedUpButton(
                    text = "Open Settings",
                    onClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.OUTLINED,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(Spacing.xs))
                ShowedUpButton(
                    text = "Continue Anyway",
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth(),
                    variant = ButtonVariant.GHOST
                )
            }

            Spacer(modifier = Modifier.height(Spacing.xxxl))
        }
    }
}

@Composable
private fun PermissionRow(
    item: PermissionItem,
    isGranted: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isGranted) item.color.copy(alpha = 0.08f)
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isGranted) item.color.copy(alpha = 0.2f)
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.md, vertical = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(item.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isGranted) Icons.Default.Check else item.icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = item.color
                )
            }

            Spacer(modifier = Modifier.width(Spacing.sm))

            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
