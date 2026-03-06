package com.showedup.app.ui.screens.calendar

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.showedup.app.data.entity.AttendanceRecordEntity
import com.showedup.app.ui.components.*
import com.showedup.app.ui.theme.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onBack: () -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calendar", fontWeight = FontWeight.Bold) },
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
            // Month navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.screenHorizontal, vertical = Spacing.sm),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.changeMonth(uiState.currentMonth.minusMonths(1))
                }) {
                    Icon(Icons.Default.ChevronLeft, "Previous Month")
                }

                Text(
                    text = "${uiState.currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${uiState.currentMonth.year}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(onClick = {
                    viewModel.changeMonth(uiState.currentMonth.plusMonths(1))
                }) {
                    Icon(Icons.Default.ChevronRight, "Next Month")
                }
            }

            // Day headers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.screenHorizontal)
            ) {
                val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.xs))

            // Calendar grid
            CalendarGrid(
                yearMonth = uiState.currentMonth,
                selectedDate = uiState.selectedDate,
                attendanceDates = uiState.attendanceDates,
                onDateSelected = { viewModel.selectDate(it) },
                modifier = Modifier.padding(horizontal = Spacing.screenHorizontal)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = Spacing.md),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            // Selected date detail
            AnimatedVisibility(
                visible = uiState.selectedDate != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = Spacing.screenHorizontal)
                ) {
                    Text(
                        text = uiState.selectedDate?.let {
                            "${it.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())}, ${it.dayOfMonth} ${it.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}"
                        } ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(Spacing.sm))

                    if (uiState.selectedDateRecords.isEmpty()) {
                        Text(
                            text = "No records for this date",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            items(uiState.selectedDateRecords) { record ->
                                RecordItem(record)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    attendanceDates: Set<String>,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val firstDayOfWeekOffset = (firstDayOfMonth.dayOfWeek.value - 1) // Monday = 0
    val daysInMonth = yearMonth.lengthOfMonth()
    val today = LocalDate.now()

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = modifier.height(((6 * 44) + 30).dp),
        userScrollEnabled = false
    ) {
        // Blank cells for offset
        items(firstDayOfWeekOffset) {
            Box(modifier = Modifier.size(44.dp))
        }

        // Day cells
        items(daysInMonth) { index ->
            val day = index + 1
            val date = yearMonth.atDay(day)
            val dateStr = date.toString()
            val isToday = date == today
            val isSelected = date == selectedDate
            val hasRecord = dateStr in attendanceDates

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .then(
                        if (isSelected) Modifier.background(Emerald500)
                        else if (isToday) Modifier.background(Emerald500.copy(alpha = 0.15f))
                        else Modifier
                    )
                    .clickable { onDateSelected(date) },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$day",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = when {
                            isSelected -> Gray950
                            isToday -> Emerald500
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                    if (hasRecord && !isSelected) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Emerald500)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecordItem(record: AttendanceRecordEntity) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(Spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.courseName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
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
                dotSize = 6.dp,
                spacing = 4.dp
            )
        }
    }
}
