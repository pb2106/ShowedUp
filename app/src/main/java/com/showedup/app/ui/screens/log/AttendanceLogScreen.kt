package com.showedup.app.ui.screens.log

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.showedup.app.data.entity.AttendanceRecordEntity
import com.showedup.app.ui.components.*
import com.showedup.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceLogScreen(
    onBack: () -> Unit,
    viewModel: AttendanceLogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance Log", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            ShowedUpTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = "Search classes…",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.screenHorizontal),
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotBlank()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Clear, "Clear")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            // Filter chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = Spacing.screenHorizontal),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                items(LogFilter.entries) { filter ->
                    PillChip(
                        label = filter.label,
                        selected = uiState.selectedFilter == filter,
                        onClick = { viewModel.setFilter(filter) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.sm))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedDots()
                }
            } else if (uiState.records.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.ListAlt,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(Spacing.md))
                        Text(
                            "No records yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // Group by date
                val grouped = uiState.records.groupBy { it.date }

                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = Spacing.screenHorizontal,
                        vertical = Spacing.xs
                    ),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    grouped.forEach { (date, records) ->
                        item {
                            Text(
                                text = formatDateHeader(date),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(
                                    top = Spacing.md,
                                    bottom = Spacing.xxs
                                )
                            )
                        }
                        items(records, key = { it.id }) { record ->
                            LogRecordCard(
                                record = record,
                                isExpanded = uiState.expandedRecordId == record.id,
                                onToggle = { viewModel.toggleExpanded(record.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LogRecordCard(
    record: AttendanceRecordEntity,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val timestamp = java.time.Instant.ofEpochMilli(record.timestamp)
        .atZone(java.time.ZoneId.systemDefault())
    val timeStr = "%02d:%02d".format(timestamp.hour, timestamp.minute)

    Surface(
        onClick = onToggle,
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Column(modifier = Modifier.padding(Spacing.cardPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = record.courseName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = timeStr,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                SignalRow(
                    state = SignalState(
                        gps = record.gpsAvailable,
                        wifi = record.wifiAvailable,
                        bluetooth = record.bluetoothAvailable,
                        audio = record.audioAvailable,
                        sensor = record.sensorAvailable
                    ),
                    dotSize = 8.dp,
                    spacing = 4.dp
                )

                Spacer(modifier = Modifier.width(Spacing.xs))

                Icon(
                    if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Toggle details",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = Spacing.sm)) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                    )
                    Spacer(modifier = Modifier.height(Spacing.sm))

                    DetailRow("GPS Signal", if (record.gpsAvailable) "Captured" else "Unavailable")
                    DetailRow("WiFi Signal", if (record.wifiAvailable) "Captured" else "Unavailable")
                    DetailRow("Bluetooth", if (record.bluetoothAvailable) "Captured" else "Unavailable")
                    DetailRow("Audio", if (record.audioAvailable) "Captured" else "Unavailable")
                    DetailRow("Sensor", if (record.sensorAvailable) "Captured" else "Unavailable")

                    Spacer(modifier = Modifier.height(Spacing.xs))

                    Text(
                        text = "Chain: ${record.chainHash.take(16)}…",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = MonoFontFamily,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = if (value == "Captured") Emerald500
            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}

private fun formatDateHeader(dateStr: String): String {
    return try {
        val date = java.time.LocalDate.parse(dateStr)
        val today = java.time.LocalDate.now()
        when {
            date == today -> "Today"
            date == today.minusDays(1) -> "Yesterday"
            else -> {
                val month = date.month.getDisplayName(
                    java.time.format.TextStyle.SHORT,
                    java.util.Locale.getDefault()
                )
                "${date.dayOfMonth} $month ${date.year}"
            }
        }
    } catch (e: Exception) {
        dateStr
    }
}
