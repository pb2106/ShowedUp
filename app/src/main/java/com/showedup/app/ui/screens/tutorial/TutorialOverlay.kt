package com.showedup.app.ui.screens.tutorial

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.showedup.app.ui.components.ShowedUpButton
import com.showedup.app.ui.navigation.Screen
import com.showedup.app.ui.theme.Emerald500
import com.showedup.app.ui.theme.Spacing

@Composable
fun TutorialOverlay(
    navController: NavController,
    onComplete: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var step by remember { mutableIntStateOf(0) }

    // Logic to advance steps based on navigation
    LaunchedEffect(currentRoute) {
        when {
            step == 0 && currentRoute == Screen.Home.route -> {
                // Initial state
            }
            step == 0 && currentRoute == Screen.Schedule.route -> step = 1
            step == 1 && currentRoute == Screen.Subjects.route -> step = 2
            step == 2 && currentRoute == Screen.Schedule.route -> step = 3
            step == 3 && currentRoute == Screen.AttendanceLog.route -> step = 4
            step == 4 && currentRoute == Screen.Home.route -> step = 5
        }
    }

    val (title, message, showButton) = when (step) {
        0 -> Triple(
            "Welcome to ShowedUp",
            "A tamper-proof attendance recorder. Let's explore!\n\nTap 'Schedule' in the bottom bar.",
            false
        )
        1 -> Triple(
            "Schedule Screen",
            "Here you can add your classes.\nTap the 'Manage Subjects' icon (book) at the top.",
            false
        )
        2 -> Triple(
            "Manage Subjects",
            "You can list all your courses here.\nLet's head back to Schedule (press 'Back').",
            false
        )
        3 -> Triple(
            "Great Job",
            "Now let's check your attendance history.\nTap 'Log' in the bottom bar.",
            false
        )
        4 -> Triple(
            "Attendance Log",
            "All your verified records appear here with their signal details.\nTap 'Home' to finish.",
            false
        )
        else -> Triple(
            "You're All Set!",
            "Enjoy using ShowedUp.",
            true
        )
    }

    // A floating box usually positioned near the top or bottom
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.lg), // padding from screen edges
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "tutorial_step"
        ) { targetStep ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .padding(bottom = if (targetStep == 5) 0.dp else 80.dp), // Avoid bottom nav bar if not finishing
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, Emerald500.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Tutorial Info",
                            tint = Emerald500
                        )
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (showButton) {
                        ShowedUpButton(
                            text = "Finish Tutorial",
                            onClick = onComplete,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
