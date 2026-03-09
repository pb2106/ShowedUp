package com.showedup.app.ui.screens.schedule

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.showedup.app.data.entity.SubjectEntity
import com.showedup.app.ui.components.AnimatedDots
import com.showedup.app.ui.components.ButtonVariant
import com.showedup.app.ui.components.ShowedUpButton
import com.showedup.app.ui.components.ShowedUpTextField
import com.showedup.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    onBack: () -> Unit,
    viewModel: SubjectsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Subjects", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.showAddDialog() }) {
                        Icon(Icons.Default.Add, "Add Subject")
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
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedDots()
                }
            } else if (uiState.subjects.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No subjects added yet.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(Spacing.screenHorizontal),
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    items(uiState.subjects, key = { it.id }) { subject ->
                        SubjectCard(
                            subject = subject,
                            onClick = { viewModel.showEditDialog(subject) },
                            onDelete = { viewModel.deleteSubject(subject) }
                        )
                    }
                }
            }
        }

        if (uiState.showAddDialog) {
            AddSubjectDialog(
                editSubject = uiState.editingSubject,
                onDismiss = { viewModel.dismissDialog() },
                onSave = { name, code, instructor, room ->
                    viewModel.saveSubject(name, code, instructor, room)
                }
            )
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun SubjectCard(
    subject: SubjectEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteOption by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { if (showDeleteOption) showDeleteOption = false else onClick() },
                onLongClick = { showDeleteOption = !showDeleteOption }
            ),
        shape = CardShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.cardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val colorOptions = listOf(Emerald500, Violet500, SignalGps, SignalWifi, SignalBluetooth)
            val subjectColor = colorOptions[subject.colorIndex % colorOptions.size]

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(subjectColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = subject.name.take(1).uppercase(),
                    color = subjectColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.width(Spacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (subject.code.isNotBlank() || subject.instructor.isNotBlank()) {
                    Text(
                        text = listOf(subject.code, subject.instructor).filter { it.isNotBlank() }.joinToString(" • "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(visible = showDeleteOption) {
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Subject",
                        tint = ErrorRed
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSubjectDialog(
    editSubject: SubjectEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(editSubject?.name ?: "") }
    var code by remember { mutableStateOf(editSubject?.code ?: "") }
    var instructor by remember { mutableStateOf(editSubject?.instructor ?: "") }
    var room by remember { mutableStateOf(editSubject?.defaultRoom ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (editSubject != null) "Edit Subject" else "Add Subject", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                ShowedUpTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Subject Name *"
                )
                ShowedUpTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = "Course Code (Optional)"
                )
                ShowedUpTextField(
                    value = instructor,
                    onValueChange = { instructor = it },
                    label = "Instructor (Optional)"
                )
                ShowedUpTextField(
                    value = room,
                    onValueChange = { room = it },
                    label = "Default Room (Optional)"
                )
            }
        },
        confirmButton = {
            ShowedUpButton(
                text = "Save",
                onClick = { if (name.isNotBlank()) onSave(name, code, instructor, room) },
                enabled = name.isNotBlank()
            )
        },
        dismissButton = {
            ShowedUpButton(text = "Cancel", onClick = onDismiss, variant = ButtonVariant.GHOST)
        }
    )
}
