package com.showedup.app.ui.screens.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.showedup.app.data.entity.SubjectEntity
import com.showedup.app.data.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SubjectsUiState(
    val subjects: List<SubjectEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showAddDialog: Boolean = false,
    val editingSubject: SubjectEntity? = null
)

@HiltViewModel
class SubjectsViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubjectsUiState())
    val uiState: StateFlow<SubjectsUiState> = _uiState.asStateFlow()

    init {
        loadSubjects()
    }

    private fun loadSubjects() {
        viewModelScope.launch {
            scheduleRepository.getAllSubjects().collect { subjects ->
                _uiState.update { it.copy(subjects = subjects, isLoading = false) }
            }
        }
    }

    fun showAddDialog() {
        _uiState.update { it.copy(showAddDialog = true, editingSubject = null) }
    }

    fun showEditDialog(subject: SubjectEntity) {
        _uiState.update { it.copy(showAddDialog = true, editingSubject = subject) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showAddDialog = false, editingSubject = null) }
    }

    fun saveSubject(name: String, code: String, instructor: String, defaultRoom: String) {
        viewModelScope.launch {
            val editing = _uiState.value.editingSubject
            if (editing != null) {
                scheduleRepository.updateSubject(
                    editing.copy(
                        name = name,
                        code = code,
                        instructor = instructor,
                        defaultRoom = defaultRoom
                    )
                )
            } else {
                scheduleRepository.addSubject(
                    SubjectEntity(
                        name = name,
                        code = code,
                        instructor = instructor,
                        defaultRoom = defaultRoom,
                        colorIndex = (Math.random() * 10).toInt()
                    )
                )
            }
            dismissDialog()
        }
    }

    fun deleteSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            scheduleRepository.deleteSubject(subject)
        }
    }
}
