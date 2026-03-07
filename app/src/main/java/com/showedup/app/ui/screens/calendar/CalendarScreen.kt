package com.showedup.app.ui.screens.calendar

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.showedup.app.data.entity.DayOffType
import com.showedup.app.data.entity.PlannedDayOffEntity
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddEventDialog() },
                containerColor = Emerald500,
                contentColor = Gray950
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
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
                plannedEventDates = uiState.plannedEventDates,
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

                    val hasRecords = uiState.selectedDateRecords.isNotEmpty()
                    val hasPlannedEvents = uiState.selectedDatePlannedEvents.isNotEmpty()

                    if (!hasRecords && !hasPlannedEvents) {
                        Text(
                            text = "No records for this date",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            // Planned events section
                            if (hasPlannedEvents) {
                                item {
                                    Text(
                                        text = "Planned Events",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = WarningAmber,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                                items(uiState.selectedDatePlannedEvents) { event ->
                                    PlannedEventItem(event)
                                }
                            }
                            // Attendance records section
                            if (hasRecords) {
                                item {
                                    Text(
                                        text = "Attendance Records",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Emerald500,
                                        modifier = Modifier.padding(
                                            top = if (hasPlannedEvents) 8.dp else 0.dp,
                                            bottom = 4.dp
                                        )
                                    )
                                }
                                items(uiState.selectedDateRecords) { record ->
                                    RecordItem(record)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add Event Dialog
        if (uiState.showAddEventDialog) {
            AddEventDialog(
                date = uiState.addEventDate,
                eventName = uiState.addEventName,
                eventType = uiState.addEventType,
                onDateChange = { viewModel.updateAddEventDate(it) },
                onNameChange = { viewModel.updateAddEventName(it) },
                onTypeChange = { viewModel.updateAddEventType(it) },
                onDismiss = { viewModel.dismissAddEventDialog() },
                onSave = { viewModel.saveEvent() }
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    yearMonth: YearMonth,
    selectedDate: LocalDate?,
    attendanceDates: Set<String>,
    plannedEventDates: Set<String>,
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
            val hasPlannedEvent = dateStr in plannedEventDates

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
                    if (!isSelected && (hasRecord || hasPlannedEvent)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (hasRecord) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(Emerald500)
                                )
                            }
                            if (hasPlannedEvent) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(WarningAmber)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlannedEventItem(event: PlannedDayOffEntity) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CardShape,
        color = WarningAmber.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(Spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Event,
                contentDescription = null,
                tint = WarningAmber,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.eventName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = event.type.name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            val badgeColor = when (event.status) {
                com.showedup.app.data.entity.PlannedDayOffStatus.PENDING -> WarningAmber
                com.showedup.app.data.entity.PlannedDayOffStatus.CONFIRMED -> Emerald500
                com.showedup.app.data.entity.PlannedDayOffStatus.CANCELLED -> ErrorRed
            }
            Box(
                modifier = Modifier
                    .clip(PillShape)
                    .background(badgeColor.copy(alpha = 0.15f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = event.status.name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = badgeColor
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEventDialog(
    date: LocalDate,
    eventName: String,
    eventType: DayOffType,
    onDateChange: (LocalDate) -> Unit,
    onNameChange: (String) -> Unit,
    onTypeChange: (DayOffType) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add Event", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                // Date picker trigger
                Surface(
                    onClick = { showDatePicker = true },
                    shape = CardShape,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.sm),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Emerald500,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${date.dayOfMonth} ${date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${date.year}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Event name
                ShowedUpTextField(
                    value = eventName,
                    onValueChange = onNameChange,
                    label = "Event Name"
                )

                // Event type chips
                Text("Type", style = MaterialTheme.typography.labelMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    val types = listOf(
                        DayOffType.EVENT to "Event",
                        DayOffType.HOLIDAY to "Holiday",
                        DayOffType.PERSONAL to "Personal",
                        DayOffType.SICK to "Sick"
                    )
                    items(types.size) { index ->
                        val (type, label) = types[index]
                        PillChip(
                            label = label,
                            selected = eventType == type,
                            onClick = { onTypeChange(type) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            ShowedUpButton(
                text = "Save",
                onClick = onSave,
                enabled = eventName.isNotBlank()
            )
        },
        dismissButton = {
            ShowedUpButton(
                text = "Cancel",
                onClick = onDismiss,
                variant = ButtonVariant.GHOST
            )
        }
    )

    // Date picker dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = date.toEpochDay() * 86400000L
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = LocalDate.ofEpochDay(millis / 86400000L)
                        onDateChange(selectedDate)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
