package com.showedup.app.ui.screens.dayoff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.showedup.app.data.entity.DayOffType
import com.showedup.app.data.entity.TimetableEntry
import com.showedup.app.data.repository.AttendanceRepository
import com.showedup.app.data.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DayOffUiState(
    val selectedType: DayOffType = DayOffType.SICK,
    val reason: String = "",
    val selectedDate: LocalDate = LocalDate.now(),
    val todayClasses: List<TimetableEntry> = emptyList(),
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false
)

@HiltViewModel
class DayOffViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DayOffUiState())
    val uiState: StateFlow<DayOffUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            scheduleRepository.getClassesByDay(LocalDate.now().dayOfWeek)
                .collect { classes ->
                    _uiState.update { it.copy(todayClasses = classes) }
                }
        }
    }

    fun selectType(type: DayOffType) {
        _uiState.update { it.copy(selectedType = type) }
    }

    fun setReason(reason: String) {
        _uiState.update { it.copy(reason = reason) }
    }

    fun submit() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            val state = _uiState.value
            attendanceRepository.insertDayOff(
                type = state.selectedType,
                date = state.selectedDate.toString(),
                reason = state.reason,
                classesSuppressed = state.todayClasses.map { it.id }
            )
            _uiState.update { it.copy(isSubmitting = false, isSubmitted = true) }
        }
    }
}
