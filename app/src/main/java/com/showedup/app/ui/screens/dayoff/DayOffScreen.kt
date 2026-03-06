package com.showedup.app.ui.screens.dayoff

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.showedup.app.data.entity.DayOffType
import com.showedup.app.ui.components.*
import com.showedup.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayOffScreen(
    onBack: () -> Unit,
    viewModel: DayOffViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Day Off", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        AnimatedContent(
            targetState = uiState.isSubmitted,
            label = "dayOffContent"
        ) { submitted ->
            if (submitted) {
                // Success state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val scale by rememberInfiniteTransition(label = "check").animateFloat(
                            initialValue = 1f,
                            targetValue = 1.1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(800),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "checkPulse"
                        )

                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .scale(scale),
                            tint = Emerald500
                        )
                        Spacer(modifier = Modifier.height(Spacing.xl))
                        Text(
                            "Day off recorded!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(Spacing.sm))
                        Text(
                            "${uiState.todayClasses.size} classes won't be tracked today",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(Spacing.xxl))
                        ShowedUpButton(
                            text = "Done",
                            onClick = onBack,
                            modifier = Modifier.fillMaxWidth(0.6f)
                        )
                    }
                }
            } else {
                // Input form
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = Spacing.screenHorizontal)
                ) {
                    Spacer(modifier = Modifier.height(Spacing.md))

                    Text(
                        "What type of day off?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(Spacing.md))

                    // Type selector chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                    ) {
                        DayOffType.entries.forEach { type ->
                            PillChip(
                                label = when (type) {
                                    DayOffType.SICK -> "Sick"
                                    DayOffType.PERSONAL -> "Personal"
                                    DayOffType.HOLIDAY -> "Holiday"
                                    DayOffType.EVENT -> "Event"
                                },
                                selected = uiState.selectedType == type,
                                onClick = { viewModel.selectType(type) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Spacing.xl))

                    ShowedUpTextField(
                        value = uiState.reason,
                        onValueChange = { viewModel.setReason(it) },
                        label = "Reason (optional)",
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(Spacing.md))

                    // Classes that will be suppressed
                    if (uiState.todayClasses.isNotEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = CardShape,
                            color = StatusDayOff.copy(alpha = 0.1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(Spacing.cardPadding)
                            ) {
                                Text(
                                    "${uiState.todayClasses.size} classes will be marked as day off:",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    color = StatusDayOff
                                )
                                Spacer(modifier = Modifier.height(Spacing.xs))
                                uiState.todayClasses.forEach { entry ->
                                    Text(
                                        "• ${entry.courseName}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    ShowedUpButton(
                        text = "Submit",
                        onClick = { viewModel.submit() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Spacing.xxl),
                        loading = uiState.isSubmitting
                    )
                }
            }
        }
    }
}
