package com.showedup.app.ui.screens.schedule

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.showedup.app.data.entity.TimetableEntry
import com.showedup.app.ui.components.*
import com.showedup.app.ui.theme.*
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    onBack: () -> Unit,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Schedule", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.showAddDialog() }) {
                        Icon(Icons.Default.Add, "Add Class")
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
            // Day toggles — for marking active/inactive days
            Text(
                text = "Active Days",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(
                    horizontal = Spacing.screenHorizontal,
                    vertical = Spacing.sm
                )
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = Spacing.screenHorizontal),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                items(DayOfWeek.entries) { day ->
                    Surface(
                        onClick = { viewModel.toggleDay(day) },
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = if (day in uiState.activeDays) Emerald500
                        else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (day !in uiState.activeDays) BorderStroke(
                            1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ) else null
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                                    .take(2),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (day in uiState.activeDays) Gray950
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = Spacing.sm),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            // Timetable grid — horizontally scrollable
            if (uiState.classes.isEmpty() && !uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(Spacing.md))
                        Text(
                            "No classes set up yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(Spacing.sm))
                        Text(
                            "Tap + to add your first class,\nor tap any cell in the table below",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // THE TIMETABLE GRID
            TimetableGrid(
                classes = uiState.classes,
                timeSlots = uiState.timeSlots,
                activeDays = uiState.activeDays,
                onCellTap = { day, slotIndex ->
                    viewModel.showAddDialogForCell(day, slotIndex)
                },
                onEntryTap = { entry ->
                    viewModel.showEditDialog(entry)
                },
                onEntryDelete = { entry ->
                    viewModel.deleteClass(entry)
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }

        // Add/Edit dialog
        if (uiState.showAddDialog) {
            AddClassDialog(
                editEntry = uiState.editingEntry,
                prefilledDay = uiState.tappedDay,
                prefilledSlot = uiState.tappedSlotIndex?.let { uiState.timeSlots.getOrNull(it) },
                onDismiss = { viewModel.dismissDialog() },
                onSave = { name, code, days, sh, sm, eh, em, instructor ->
                    viewModel.saveClass(name, code, days, sh, sm, eh, em, instructor)
                }
            )
        }
    }
}

/**
 * College-style timetable grid.
 * Rows = days (Mon–Sat), Columns = time slots.
 * Horizontally scrollable. Each cell shows the class or is empty (tappable).
 */
@Composable
private fun TimetableGrid(
    classes: List<TimetableEntry>,
    timeSlots: List<TimeSlot>,
    activeDays: Set<DayOfWeek>,
    onCellTap: (DayOfWeek, Int) -> Unit,
    onEntryTap: (TimetableEntry) -> Unit,
    onEntryDelete: (TimetableEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    val daysToShow = listOf(
        DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY
    )

    val dayLabelWidth = 48.dp
    val cellWidth = 90.dp
    val cellHeight = 64.dp
    val headerHeight = 40.dp

    val grouped = classes.groupBy { it.dayOfWeek }

    val scrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    Column(modifier = modifier) {
        // Header row (time slot labels)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Spacing.screenHorizontal)
        ) {
            // Corner spacer
            Box(
                modifier = Modifier
                    .width(dayLabelWidth)
                    .height(headerHeight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Day",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
            ) {
                timeSlots.forEachIndexed { index, slot ->
                    Box(
                        modifier = Modifier
                            .width(cellWidth)
                            .height(headerHeight)
                            .border(
                                0.5.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = slot.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // Grid rows (one per day)
        Column(
            modifier = Modifier
                .verticalScroll(verticalScrollState)
                .padding(start = Spacing.screenHorizontal)
        ) {
            daysToShow.forEach { day ->
                val dayClasses = grouped[day] ?: emptyList()
                val isActive = day in activeDays

                Row(modifier = Modifier.fillMaxWidth()) {
                    // Day label
                    Box(
                        modifier = Modifier
                            .width(dayLabelWidth)
                            .height(cellHeight)
                            .background(
                                if (isActive) Emerald500.copy(alpha = 0.05f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                            )
                            .border(
                                0.5.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                                .take(3),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isActive) Emerald500
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }

                    // Cells — horizontally scroll in sync with header
                    Row(
                        modifier = Modifier
                            .horizontalScroll(scrollState)
                    ) {
                        timeSlots.forEachIndexed { slotIndex, slot ->
                            val matchingEntry = dayClasses.find { entry ->
                                // A class matches this slot if their time ranges overlap
                                entry.startTimeMinutes < slot.endTimeMinutes &&
                                        entry.endTimeMinutes > slot.startTimeMinutes
                            }

                            TimetableCell(
                                entry = matchingEntry,
                                isActive = isActive,
                                width = cellWidth,
                                height = cellHeight,
                                onEmptyTap = { onCellTap(day, slotIndex) },
                                onEntryTap = { onEntryTap(matchingEntry!!) },
                                onDelete = { onEntryDelete(matchingEntry!!) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TimetableCell(
    entry: TimetableEntry?,
    isActive: Boolean,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    onEmptyTap: () -> Unit,
    onEntryTap: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteOption by remember { mutableStateOf(false) }

    val cellColor = if (entry != null) {
        // Assign color based on course name hash for visual distinction
        val colorIndex = (entry.courseName.hashCode() and 0x7FFFFFFF) % cellColors.size
        cellColors[colorIndex].copy(alpha = if (isActive) 0.18f else 0.08f)
    } else {
        if (isActive) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.08f)
        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.04f)
    }

    val borderColor = if (entry != null) {
        val colorIndex = (entry.courseName.hashCode() and 0x7FFFFFFF) % cellColors.size
        cellColors[colorIndex].copy(alpha = 0.3f)
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
    }

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(cellColor)
            .border(0.5.dp, borderColor)
            .combinedClickable(
                onClick = {
                    if (entry != null) {
                        if (showDeleteOption) showDeleteOption = false
                        else onEntryTap()
                    } else {
                        onEmptyTap()
                    }
                },
                onLongClick = {
                    if (entry != null) showDeleteOption = !showDeleteOption
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (entry != null) {
            if (showDeleteOption) {
                IconButton(
                    onClick = {
                        showDeleteOption = false
                        onDelete()
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = ErrorRed,
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = entry.courseCode.ifBlank { entry.courseName },
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (entry.instructor.isNotBlank()) {
                        Text(
                            text = entry.instructor,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 7.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        } else if (isActive) {
            // Empty active cell — show a faint + 
            Icon(
                Icons.Default.Add,
                contentDescription = "Add class",
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
            )
        }
    }
}

// Distinct colors for different courses
private val cellColors = listOf(
    SignalGps,      // cyan
    SignalWifi,     // indigo
    SignalBluetooth,// teal
    SignalAudio,    // orange
    SignalSensor,   // pink
    Emerald500,     // green
    Violet500,      // purple
    StatusHoliday,  // blue
    WarningAmber,   // amber
    StatusAbsent,   // red
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddClassDialog(
    editEntry: TimetableEntry?,
    prefilledDay: DayOfWeek?,
    prefilledSlot: TimeSlot?,
    onDismiss: () -> Unit,
    onSave: (String, String, Set<DayOfWeek>, Int, Int, Int, Int, String) -> Unit
) {
    val isEditing = editEntry != null
    var courseName by remember { mutableStateOf(editEntry?.courseName ?: "") }
    var courseCode by remember { mutableStateOf(editEntry?.courseCode ?: "") }
    var selectedDays by remember {
        mutableStateOf(
            when {
                editEntry != null -> setOf(editEntry.dayOfWeek)
                prefilledDay != null -> setOf(prefilledDay)
                else -> emptySet<DayOfWeek>()
            }
        )
    }
    var startHour by remember {
        mutableIntStateOf(
            editEntry?.let { it.startTimeMinutes / 60 }
                ?: prefilledSlot?.startHour
                ?: 9
        )
    }
    var startMinute by remember {
        mutableIntStateOf(
            editEntry?.let { it.startTimeMinutes % 60 }
                ?: prefilledSlot?.startMinute
                ?: 0
        )
    }
    var endHour by remember {
        mutableIntStateOf(
            editEntry?.let { it.endTimeMinutes / 60 }
                ?: prefilledSlot?.endHour
                ?: 10
        )
    }
    var endMinute by remember {
        mutableIntStateOf(
            editEntry?.let { it.endTimeMinutes % 60 }
                ?: prefilledSlot?.endMinute
                ?: 0
        )
    }
    var instructor by remember { mutableStateOf(editEntry?.instructor ?: "") }

    // Time picker states
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (isEditing) "Edit Class" else "Add Class",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                ShowedUpTextField(
                    value = courseName,
                    onValueChange = { courseName = it },
                    label = "Class Name"
                )
                ShowedUpTextField(
                    value = courseCode,
                    onValueChange = { courseCode = it },
                    label = "Course Code"
                )

                // Day selector
                Text(
                    if (isEditing) "Day" else "Days (select multiple)",
                    style = MaterialTheme.typography.labelMedium
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(DayOfWeek.entries) { day ->
                        PillChip(
                            label = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            selected = day in selectedDays,
                            onClick = {
                                selectedDays = if (isEditing) {
                                    setOf(day)
                                } else {
                                    if (day in selectedDays) selectedDays - day
                                    else selectedDays + day
                                }
                            }
                        )
                    }
                }

                // Time — tappable to open time picker
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Surface(
                        onClick = { showStartTimePicker = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outline
                        ),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = Spacing.md,
                                vertical = Spacing.sm
                            )
                        ) {
                            Text(
                                "Start",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "%02d:%02d".format(startHour, startMinute),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Surface(
                        onClick = { showEndTimePicker = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outline
                        ),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = Spacing.md,
                                vertical = Spacing.sm
                            )
                        ) {
                            Text(
                                "End",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "%02d:%02d".format(endHour, endMinute),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                ShowedUpTextField(
                    value = instructor,
                    onValueChange = { instructor = it },
                    label = "Instructor"
                )
            }
        },
        confirmButton = {
            ShowedUpButton(
                text = "Save",
                onClick = {
                    if (courseName.isNotBlank() && selectedDays.isNotEmpty()) {
                        onSave(
                            courseName, courseCode, selectedDays,
                            startHour, startMinute, endHour, endMinute,
                            instructor
                        )
                    }
                },
                enabled = courseName.isNotBlank() && selectedDays.isNotEmpty()
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

    // Start time picker dialog
    if (showStartTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = startHour,
            initialMinute = startMinute,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { showStartTimePicker = false },
            title = { Text("Start Time") },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                ShowedUpButton(
                    text = "OK",
                    onClick = {
                        startHour = timePickerState.hour
                        startMinute = timePickerState.minute
                        showStartTimePicker = false
                    }
                )
            },
            dismissButton = {
                ShowedUpButton(
                    text = "Cancel",
                    onClick = { showStartTimePicker = false },
                    variant = ButtonVariant.GHOST
                )
            }
        )
    }

    // End time picker dialog
    if (showEndTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = endHour,
            initialMinute = endMinute,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { showEndTimePicker = false },
            title = { Text("End Time") },
            text = {
                TimePicker(state = timePickerState)
            },
            confirmButton = {
                ShowedUpButton(
                    text = "OK",
                    onClick = {
                        endHour = timePickerState.hour
                        endMinute = timePickerState.minute
                        showEndTimePicker = false
                    }
                )
            },
            dismissButton = {
                ShowedUpButton(
                    text = "Cancel",
                    onClick = { showEndTimePicker = false },
                    variant = ButtonVariant.GHOST
                )
            }
        )
    }
}
