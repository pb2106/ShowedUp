package com.showedup.app.ui.screens.schedule

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
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
                    val isActive = day in uiState.activeDays
                    val scale by animateFloatAsState(
                        targetValue = if (isActive) 1f else 0.95f,
                        label = "dayScale"
                    )

                    Surface(
                        onClick = { viewModel.toggleDay(day) },
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = if (isActive) Emerald500 else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (!isActive) androidx.compose.foundation.BorderStroke(
                            1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        ) else null
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                                    .take(2),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isActive) Gray950
                                else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = Spacing.md),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )

            // Class list
            if (uiState.classes.isEmpty()) {
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
                        ShowedUpButton(
                            text = "Add Class",
                            onClick = { viewModel.showAddDialog() },
                            variant = ButtonVariant.OUTLINED
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        horizontal = Spacing.screenHorizontal,
                        vertical = Spacing.xs
                    ),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    val grouped = uiState.classes.groupBy { it.dayOfWeek }
                    DayOfWeek.entries.forEach { day ->
                        val dayClasses = grouped[day]
                        if (!dayClasses.isNullOrEmpty()) {
                            item {
                                Text(
                                    text = day.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (day in uiState.activeDays)
                                        MaterialTheme.colorScheme.onSurface
                                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.padding(top = Spacing.xs)
                                )
                            }
                            items(dayClasses, key = { it.id }) { entry ->
                                ScheduleClassCard(
                                    entry = entry,
                                    isActive = day in uiState.activeDays,
                                    onEdit = { viewModel.showEditDialog(entry) },
                                    onDelete = { viewModel.deleteClass(entry) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Add/Edit dialog
        if (uiState.showAddDialog) {
            AddClassDialog(
                editEntry = uiState.editingEntry,
                onDismiss = { viewModel.dismissDialog() },
                onSave = viewModel::saveClass
            )
        }
    }
}

@Composable
private fun ScheduleClassCard(
    entry: TimetableEntry,
    isActive: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val startHour = entry.startTimeMinutes / 60
    val startMin = entry.startTimeMinutes % 60
    val endHour = entry.endTimeMinutes / 60
    val endMin = entry.endTimeMinutes % 60

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit),
        shape = CardShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(
            alpha = if (isActive) 0.3f else 0.15f
        )
    ) {
        Row(
            modifier = Modifier.padding(Spacing.cardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.courseName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isActive) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
                Text(
                    text = "%02d:%02d – %02d:%02d".format(startHour, startMin, endHour, endMin),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (entry.room.isNotBlank()) {
                    Text(
                        text = entry.room,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    "Delete",
                    tint = ErrorRed.copy(alpha = 0.7f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun AddClassDialog(
    editEntry: TimetableEntry?,
    onDismiss: () -> Unit,
    onSave: (String, String, DayOfWeek, Int, Int, Int, Int, String, String) -> Unit
) {
    var courseName by remember { mutableStateOf(editEntry?.courseName ?: "") }
    var courseCode by remember { mutableStateOf(editEntry?.courseCode ?: "") }
    var selectedDay by remember {
        mutableStateOf(editEntry?.dayOfWeek ?: DayOfWeek.MONDAY)
    }
    var startHour by remember {
        mutableIntStateOf(editEntry?.let { it.startTimeMinutes / 60 } ?: 9)
    }
    var startMinute by remember {
        mutableIntStateOf(editEntry?.let { it.startTimeMinutes % 60 } ?: 0)
    }
    var endHour by remember {
        mutableIntStateOf(editEntry?.let { it.endTimeMinutes / 60 } ?: 10)
    }
    var endMinute by remember {
        mutableIntStateOf(editEntry?.let { it.endTimeMinutes % 60 } ?: 0)
    }
    var room by remember { mutableStateOf(editEntry?.room ?: "") }
    var instructor by remember { mutableStateOf(editEntry?.instructor ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (editEntry != null) "Edit Class" else "Add Class",
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
                Text("Day", style = MaterialTheme.typography.labelMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(DayOfWeek.entries) { day ->
                        PillChip(
                            label = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            selected = day == selectedDay,
                            onClick = { selectedDay = day }
                        )
                    }
                }

                // Time
                Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                    ShowedUpTextField(
                        value = "%02d:%02d".format(startHour, startMinute),
                        onValueChange = {},
                        label = "Start",
                        readOnly = true,
                        modifier = Modifier.weight(1f)
                    )
                    ShowedUpTextField(
                        value = "%02d:%02d".format(endHour, endMinute),
                        onValueChange = {},
                        label = "End",
                        readOnly = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                ShowedUpTextField(
                    value = room,
                    onValueChange = { room = it },
                    label = "Room"
                )
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
                    if (courseName.isNotBlank()) {
                        onSave(
                            courseName, courseCode, selectedDay,
                            startHour, startMinute, endHour, endMinute,
                            room, instructor
                        )
                    }
                },
                enabled = courseName.isNotBlank()
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
}
