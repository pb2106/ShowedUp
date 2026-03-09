package com.showedup.app.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.showedup.app.data.entity.TimetableEntry
import com.showedup.app.ui.components.*
import com.showedup.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCalendar: () -> Unit,
    onNavigateToSchedule: () -> Unit,
    onNavigateToLog: () -> Unit,
    onNavigateToExport: () -> Unit,
    onNavigateToDayOff: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = uiState.greeting,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Today - ${uiState.currentDateFormatted}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Filled.Home, "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToCalendar,
                    icon = { Icon(Icons.Outlined.CalendarMonth, "Calendar") },
                    label = { Text("Calendar") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToSchedule,
                    icon = { Icon(Icons.Outlined.Schedule, "Schedule") },
                    label = { Text("Schedule") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToLog,
                    icon = { Icon(Icons.Outlined.ListAlt, "Log") },
                    label = { Text("Log") }
                )
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                AnimatedDots()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    horizontal = Spacing.screenHorizontal,
                    vertical = Spacing.md
                ),
                verticalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {
                // Summary card
                item {
                    SummaryCard(
                        attended = uiState.attendedCount,
                        total = uiState.totalCount
                    )
                }

                // Quick actions
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        QuickActionChip(
                            icon = Icons.Outlined.EventBusy,
                            label = "Day Off",
                            onClick = onNavigateToDayOff,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionChip(
                            icon = Icons.Outlined.PictureAsPdf,
                            label = "Export",
                            onClick = onNavigateToExport,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Today's classes header
                item {
                    Text(
                        text = "Today's Classes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = Spacing.sm)
                    )
                }

                if (uiState.todayClasses.isEmpty()) {
                    item {
                        EmptyStateCard(
                            message = "No classes today",
                            icon = Icons.Outlined.EventAvailable
                        )
                    }
                } else {
                    items(uiState.todayClasses, key = { it.id }) { entry ->
                        val isAttended = uiState.todayRecords.any {
                            it.timetableEntryId == entry.id
                        }
                        ClassCard(entry = entry, isAttended = isAttended)
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(attended: Int, total: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.cardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (total == 0) "No classes today"
                    else if (attended >= total) "All done for today!"
                    else "$attended of $total classes recorded",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (total > 0) {
                    Spacer(modifier = Modifier.height(Spacing.xs))
                    LinearProgressIndicator(
                        progress = { if (total > 0) attended.toFloat() / total else 0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(PillShape),
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        color = Emerald500
                    )
                }
            }

            Spacer(modifier = Modifier.width(Spacing.md))

            Surface(
                shape = CircleShape,
                color = if (attended >= total && total > 0) Emerald500.copy(alpha = 0.15f)
                else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(
                    modifier = Modifier.size(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val percentage = if (total > 0) (attended * 100) / total else 0
                    Text(
                        text = if (total > 0) "$percentage%" else "-",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (attended >= total && total > 0) Emerald500
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = CardShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(Spacing.xs))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ClassCard(entry: TimetableEntry, isAttended: Boolean) {
    val startHour = entry.startTimeMinutes / 60
    val startMin = entry.startTimeMinutes % 60
    val endHour = entry.endTimeMinutes / 60
    val endMin = entry.endTimeMinutes % 60
    val timeStr = "%02d:%02d – %02d:%02d".format(startHour, startMin, endHour, endMin)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isAttended) Emerald500.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.cardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time indicator
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .clip(PillShape)
                    .background(
                        if (isAttended) Emerald500 else MaterialTheme.colorScheme.outline
                    )
            )

            Spacer(modifier = Modifier.width(Spacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.courseName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(Spacing.xxs))
                Text(
                    text = timeStr,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            StatusBadge(
                status = if (isAttended) AttendanceStatus.PRESENT
                else AttendanceStatus.UPCOMING
            )
        }
    }
}

@Composable
private fun EmptyStateCard(
    message: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.xxl),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(Spacing.md))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
