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
import com.showedup.app.data.entity.SubjectEntity
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
    onNavigateToSubjects: () -> Unit,
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
                    IconButton(onClick = onNavigateToSubjects) {
                        Icon(Icons.Default.MenuBook, "Manage Subjects")
                    }
                    // Edit time slots button
                    IconButton(onClick = { viewModel.showSlotEditor() }) {
                        Icon(Icons.Default.EditCalendar, "Edit Periods")
                    }
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
            // Day toggles
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

            // Timetable grid
            if (uiState.classes.isEmpty() && !uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.xxl),
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
                            "Tap any cell to add a class",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

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

        if (uiState.showAddDialog) {
            AddClassDialog(
                editEntry = uiState.editingEntry,
                subjects = uiState.subjects,
                prefilledDay = uiState.tappedDay,
                prefilledSlot = uiState.tappedSlotIndex?.let { uiState.timeSlots.getOrNull(it) },
                onDismiss = { viewModel.dismissDialog() },
                onSave = { name, code, days, sh, sm, eh, em, instructor ->
                    viewModel.saveClass(name, code, days, sh, sm, eh, em, instructor)
                }
            )
        }

        // Slot editor dialog
        if (uiState.showSlotEditor) {
            SlotEditorDialog(
                timeSlots = uiState.timeSlots,
                editingIndex = uiState.editingSlotIndex,
                onAddSlot = { viewModel.addSlot(it) },
                onUpdateSlot = { i, s -> viewModel.updateSlot(i, s) },
                onDeleteSlot = { viewModel.deleteSlot(it) },
                onEditSlot = { viewModel.showSlotEditor(it) },
                onDismiss = { viewModel.dismissSlotEditor() }
            )
        }
    }
}

// ─── Timetable Grid ─────────────────────────────────────────────

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
    val breakCellWidth = 44.dp
    val cellHeight = 64.dp
    val headerHeight = 48.dp

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

            Row(modifier = Modifier.horizontalScroll(scrollState)) {
                timeSlots.forEach { slot ->
                    val w = if (slot.isBreak) breakCellWidth else cellWidth
                    Box(
                        modifier = Modifier
                            .width(w)
                            .height(headerHeight)
                            .border(
                                0.5.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                            )
                            .then(
                                if (slot.isBreak) Modifier.background(
                                    WarningAmber.copy(alpha = 0.06f)
                                ) else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = slot.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = if (slot.isBreak) 8.sp else 10.sp,
                            color = if (slot.isBreak) WarningAmber.copy(alpha = 0.7f)
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            lineHeight = 11.sp
                        )
                    }
                }
            }
        }

        // Grid rows
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

                    // Cells
                    Row(modifier = Modifier.horizontalScroll(scrollState)) {
                        timeSlots.forEachIndexed { slotIndex, slot ->
                            val w = if (slot.isBreak) breakCellWidth else cellWidth

                            if (slot.isBreak) {
                                // Break cell — just show a hatch/tint, no class
                                Box(
                                    modifier = Modifier
                                        .width(w)
                                        .height(cellHeight)
                                        .background(WarningAmber.copy(alpha = 0.04f))
                                        .border(
                                            0.5.dp,
                                            MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                                        )
                                )
                            } else {
                                val matchingEntry = dayClasses.find { entry ->
                                    entry.startTimeMinutes < slot.endTimeMinutes &&
                                            entry.endTimeMinutes > slot.startTimeMinutes
                                }

                                TimetableCell(
                                    entry = matchingEntry,
                                    isActive = isActive,
                                    width = w,
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
                    } else onEmptyTap()
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
                        text = entry.courseName,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (entry.courseCode.isNotBlank()) {
                        Text(
                            text = entry.courseCode,
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
            Icon(
                Icons.Default.Add,
                contentDescription = "Add class",
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
            )
        }
    }
}

private val cellColors = listOf(
    SignalGps, SignalWifi, SignalBluetooth, SignalAudio, SignalSensor,
    Emerald500, Violet500, StatusHoliday, WarningAmber, StatusAbsent,
)

// ─── Add/Edit Class Dialog ──────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddClassDialog(
    editEntry: TimetableEntry?,
    subjects: List<SubjectEntity>,
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
        mutableIntStateOf(editEntry?.let { it.startTimeMinutes / 60 } ?: prefilledSlot?.startHour ?: 9)
    }
    var startMinute by remember {
        mutableIntStateOf(editEntry?.let { it.startTimeMinutes % 60 } ?: prefilledSlot?.startMinute ?: 0)
    }
    var endHour by remember {
        mutableIntStateOf(editEntry?.let { it.endTimeMinutes / 60 } ?: prefilledSlot?.endHour ?: 10)
    }
    var endMinute by remember {
        mutableIntStateOf(editEntry?.let { it.endTimeMinutes % 60 } ?: prefilledSlot?.endMinute ?: 0)
    }
    var instructor by remember { mutableStateOf(editEntry?.instructor ?: "") }

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
                if (!isEditing && subjects.isNotEmpty()) {
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = "Select Subject to Auto-fill",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            subjects.forEach { subject ->
                                DropdownMenuItem(
                                    text = { Text(subject.name) },
                                    onClick = {
                                        courseName = subject.name
                                        if (subject.code.isNotBlank()) courseCode = subject.code
                                        if (subject.instructor.isNotBlank()) instructor = subject.instructor
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                
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
                                selectedDays = if (isEditing) setOf(day)
                                else {
                                    if (day in selectedDays) selectedDays - day
                                    else selectedDays + day
                                }
                            }
                        )
                    }
                }

                // Time — tappable with 12h display
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Surface(
                        onClick = { showStartTimePicker = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm)
                        ) {
                            Text(
                                "Start",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                formatTime12(startHour, startMinute),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Surface(
                        onClick = { showEndTimePicker = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = Spacing.md, vertical = Spacing.sm)
                        ) {
                            Text(
                                "End",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                formatTime12(endHour, endMinute),
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
                        onSave(courseName, courseCode, selectedDays, startHour, startMinute, endHour, endMinute, instructor)
                    }
                },
                enabled = courseName.isNotBlank() && selectedDays.isNotEmpty()
            )
        },
        dismissButton = {
            ShowedUpButton(text = "Cancel", onClick = onDismiss, variant = ButtonVariant.GHOST)
        }
    )

    // Time pickers — 12h format
    if (showStartTimePicker) {
        TimePickerDialog(
            title = "Start Time",
            initialHour = startHour,
            initialMinute = startMinute,
            onConfirm = { h, m -> startHour = h; startMinute = m; showStartTimePicker = false },
            onDismiss = { showStartTimePicker = false }
        )
    }
    if (showEndTimePicker) {
        TimePickerDialog(
            title = "End Time",
            initialHour = endHour,
            initialMinute = endMinute,
            onConfirm = { h, m -> endHour = h; endMinute = m; showEndTimePicker = false },
            onDismiss = { showEndTimePicker = false }
        )
    }
}

// ─── Slot Editor Dialog ─────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SlotEditorDialog(
    timeSlots: List<TimeSlot>,
    editingIndex: Int?,
    onAddSlot: (TimeSlot) -> Unit,
    onUpdateSlot: (Int, TimeSlot) -> Unit,
    onDeleteSlot: (Int) -> Unit,
    onEditSlot: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    // If editing a specific slot, show the edit form
    if (editingIndex != null) {
        val slot = timeSlots.getOrNull(editingIndex) ?: return
        SlotEditForm(
            slot = slot,
            onSave = { updated -> onUpdateSlot(editingIndex, updated) },
            onDismiss = onDismiss
        )
        return
    }

    // Otherwise show the slot list with add option
    var showAddForm by remember { mutableStateOf(false) }

    if (showAddForm) {
        SlotEditForm(
            slot = null,
            onSave = { onAddSlot(it); showAddForm = false },
            onDismiss = { showAddForm = false }
        )
        return
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Periods", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.xs)) {
                Text(
                    "Tap a period to edit, swipe left to delete",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(Spacing.xs))

                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .heightIn(max = 300.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xxs)
                ) {
                    timeSlots.forEachIndexed { index, slot ->
                        Surface(
                            onClick = { onEditSlot(index) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (slot.isBreak) WarningAmber.copy(alpha = 0.08f)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            border = BorderStroke(
                                0.5.dp,
                                if (slot.isBreak) WarningAmber.copy(alpha = 0.2f)
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = Spacing.sm, vertical = Spacing.xs),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (slot.isBreak) slot.breakLabel.replace("\n", " ")
                                        else "Period ${index + 1}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (slot.isBreak) WarningAmber
                                        else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = slot.rangeLabel,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(
                                    onClick = { onDeleteSlot(index) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Delete",
                                        tint = ErrorRed.copy(alpha = 0.6f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            ShowedUpButton(
                text = "Add Period",
                onClick = { showAddForm = true },
                variant = ButtonVariant.OUTLINED,
                icon = {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                }
            )
        },
        dismissButton = {
            ShowedUpButton(text = "Done", onClick = onDismiss, variant = ButtonVariant.PRIMARY)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SlotEditForm(
    slot: TimeSlot?,
    onSave: (TimeSlot) -> Unit,
    onDismiss: () -> Unit
) {
    val isNew = slot == null
    var sh by remember { mutableIntStateOf(slot?.startHour ?: 9) }
    var sm by remember { mutableIntStateOf(slot?.startMinute ?: 0) }
    var eh by remember { mutableIntStateOf(slot?.endHour ?: 10) }
    var em by remember { mutableIntStateOf(slot?.endMinute ?: 0) }
    var isBreak by remember { mutableStateOf(slot?.isBreak ?: false) }
    var breakLabel by remember { mutableStateOf(slot?.breakLabel?.replace("\n", " ") ?: "Break") }

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (isNew) "Add Period" else "Edit Period",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                // Start/End time
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    Surface(
                        onClick = { showStartPicker = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(modifier = Modifier.padding(Spacing.sm)) {
                            Text("Start", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatTime12(sh, sm), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Surface(
                        onClick = { showEndPicker = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(modifier = Modifier.padding(Spacing.sm)) {
                            Text("End", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatTime12(eh, em), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                // Break toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "This is a break",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isBreak,
                        onCheckedChange = { isBreak = it },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = WarningAmber,
                            checkedThumbColor = White
                        )
                    )
                }

                if (isBreak) {
                    ShowedUpTextField(
                        value = breakLabel,
                        onValueChange = { breakLabel = it },
                        label = "Break Name"
                    )
                }
            }
        },
        confirmButton = {
            ShowedUpButton(
                text = "Save",
                onClick = {
                    onSave(
                        TimeSlot(
                            startHour = sh, startMinute = sm,
                            endHour = eh, endMinute = em,
                            isBreak = isBreak,
                            breakLabel = if (isBreak) breakLabel else ""
                        )
                    )
                }
            )
        },
        dismissButton = {
            ShowedUpButton(text = "Cancel", onClick = onDismiss, variant = ButtonVariant.GHOST)
        }
    )

    if (showStartPicker) {
        TimePickerDialog(
            title = "Start Time",
            initialHour = sh, initialMinute = sm,
            onConfirm = { h, m -> sh = h; sm = m; showStartPicker = false },
            onDismiss = { showStartPicker = false }
        )
    }
    if (showEndPicker) {
        TimePickerDialog(
            title = "End Time",
            initialHour = eh, initialMinute = em,
            onConfirm = { h, m -> eh = h; em = m; showEndPicker = false },
            onDismiss = { showEndPicker = false }
        )
    }
}

// ─── Shared Time Picker Dialog (12h format) ─────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    title: String,
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = false
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { TimePicker(state = state) },
        confirmButton = {
            ShowedUpButton(
                text = "OK",
                onClick = { onConfirm(state.hour, state.minute) }
            )
        },
        dismissButton = {
            ShowedUpButton(text = "Cancel", onClick = onDismiss, variant = ButtonVariant.GHOST)
        }
    )
}

// ─── Helpers ────────────────────────────────────────────────────

private fun formatTime12(hour: Int, minute: Int): String {
    val h12 = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
    val ampm = if (hour >= 12) "PM" else "AM"
    return if (minute == 0) "$h12:00 $ampm"
    else "%d:%02d %s".format(h12, minute, ampm)
}
