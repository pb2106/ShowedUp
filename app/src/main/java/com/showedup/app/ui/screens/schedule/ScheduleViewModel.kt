package com.showedup.app.ui.screens.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.showedup.app.data.entity.TimetableEntry
import com.showedup.app.data.entity.WeeklyScheduleEntity
import com.showedup.app.data.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

data class ScheduleUiState(
    val classes: List<TimetableEntry> = emptyList(),
    val weeklySchedule: WeeklyScheduleEntity? = null,
    val activeDays: Set<DayOfWeek> = DayOfWeek.entries.toSet(),
    val isLoading: Boolean = true,
    val showAddDialog: Boolean = false,
    val editingEntry: TimetableEntry? = null
)

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    init {
        loadSchedule()
    }

    private fun loadSchedule() {
        viewModelScope.launch {
            scheduleRepository.getAllClasses()
                .combine(scheduleRepository.getLatestWeeklySchedule()) { classes, schedule ->
                    val activeDays = schedule?.activeDays
                        ?.split(",")
                        ?.filter { it.isNotBlank() }
                        ?.map { DayOfWeek.of(it.trim().toInt()) }
                        ?.toSet()
                        ?: DayOfWeek.entries.toSet()

                    ScheduleUiState(
                        classes = classes,
                        weeklySchedule = schedule,
                        activeDays = activeDays,
                        isLoading = false
                    )
                }
                .collect { _uiState.value = it }
        }
    }

    fun toggleDay(day: DayOfWeek) {
        val current = _uiState.value.activeDays.toMutableSet()
        if (day in current) current.remove(day) else current.add(day)
        _uiState.update { it.copy(activeDays = current) }

        viewModelScope.launch {
            scheduleRepository.setWeeklySchedule(
                activeDays = current.toList(),
                effectiveFrom = LocalDate.now().toString()
            )
        }
    }

    fun showAddDialog() {
        _uiState.update { it.copy(showAddDialog = true, editingEntry = null) }
    }

    fun showEditDialog(entry: TimetableEntry) {
        _uiState.update { it.copy(showAddDialog = true, editingEntry = entry) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showAddDialog = false, editingEntry = null) }
    }

    fun saveClass(
        courseName: String,
        courseCode: String,
        dayOfWeek: DayOfWeek,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        room: String,
        instructor: String
    ) {
        viewModelScope.launch {
            val entry = _uiState.value.editingEntry?.copy(
                courseName = courseName,
                courseCode = courseCode,
                dayOfWeek = dayOfWeek,
                startTimeMinutes = startHour * 60 + startMinute,
                endTimeMinutes = endHour * 60 + endMinute,
                room = room,
                instructor = instructor
            ) ?: TimetableEntry(
                courseName = courseName,
                courseCode = courseCode,
                dayOfWeek = dayOfWeek,
                startTimeMinutes = startHour * 60 + startMinute,
                endTimeMinutes = endHour * 60 + endMinute,
                room = room,
                instructor = instructor
            )

            if (_uiState.value.editingEntry != null) {
                scheduleRepository.updateClass(entry)
            } else {
                scheduleRepository.addClass(entry)
            }
            dismissDialog()
        }
    }

    fun deleteClass(entry: TimetableEntry) {
        viewModelScope.launch {
            scheduleRepository.deleteClass(entry)
        }
    }
}
