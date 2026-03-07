package com.showedup.app.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.showedup.app.data.entity.AttendanceRecordEntity
import com.showedup.app.data.entity.DayOffType
import com.showedup.app.data.entity.PlannedDayOffEntity
import com.showedup.app.data.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class CalendarUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate? = null,
    val attendanceDates: Set<String> = emptySet(),
    val dayOffDates: Set<String> = emptySet(),
    val plannedEventDates: Set<String> = emptySet(),
    val selectedDateRecords: List<AttendanceRecordEntity> = emptyList(),
    val selectedDatePlannedEvents: List<PlannedDayOffEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showAddEventDialog: Boolean = false,
    val editingEvent: PlannedDayOffEntity? = null,
    val addEventDate: LocalDate = LocalDate.now(),
    val addEventName: String = "",
    val addEventType: DayOffType = DayOffType.EVENT
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        loadDates()
        loadPlannedEvents()
    }

    private fun loadDates() {
        viewModelScope.launch {
            attendanceRepository.getAllDates().collect { dates ->
                _uiState.update {
                    it.copy(
                        attendanceDates = dates.toSet(),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun loadPlannedEvents() {
        viewModelScope.launch {
            attendanceRepository.getAllPlannedDayOffs().collect { events ->
                _uiState.update {
                    it.copy(
                        plannedEventDates = events.map { e -> e.targetDate }.toSet()
                    )
                }
            }
        }
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        viewModelScope.launch {
            attendanceRepository.getRecordsByDate(date.toString())
                .collect { records ->
                    _uiState.update { it.copy(selectedDateRecords = records) }
                }
        }
        viewModelScope.launch {
            attendanceRepository.getAllPlannedDayOffs().collect { allEvents ->
                val dateStr = date.toString()
                _uiState.update {
                    it.copy(
                        selectedDatePlannedEvents = allEvents.filter { e -> e.targetDate == dateStr }
                    )
                }
            }
        }
    }

    fun changeMonth(yearMonth: YearMonth) {
        _uiState.update { it.copy(currentMonth = yearMonth) }
    }

    fun showAddEventDialog() {
        val prefilledDate = _uiState.value.selectedDate ?: LocalDate.now()
        // If selected date is in the past, default to today
        val safeDate = if (prefilledDate.isBefore(LocalDate.now())) LocalDate.now() else prefilledDate
        _uiState.update {
            it.copy(
                showAddEventDialog = true,
                editingEvent = null,
                addEventDate = safeDate,
                addEventName = "",
                addEventType = DayOffType.EVENT
            )
        }
    }

    fun showEditEventDialog(event: PlannedDayOffEntity) {
        _uiState.update {
            it.copy(
                showAddEventDialog = true,
                editingEvent = event,
                addEventDate = LocalDate.parse(event.targetDate),
                addEventName = event.eventName,
                addEventType = event.type
            )
        }
    }

    fun dismissAddEventDialog() {
        _uiState.update { it.copy(showAddEventDialog = false, editingEvent = null) }
    }

    fun updateAddEventDate(date: LocalDate) {
        // Block past dates
        if (date.isBefore(LocalDate.now())) return
        _uiState.update { it.copy(addEventDate = date) }
    }

    fun updateAddEventName(name: String) {
        _uiState.update { it.copy(addEventName = name) }
    }

    fun updateAddEventType(type: DayOffType) {
        _uiState.update { it.copy(addEventType = type) }
    }

    fun saveEvent() {
        val state = _uiState.value
        if (state.addEventName.isBlank()) return
        // Block past dates
        if (state.addEventDate.isBefore(LocalDate.now())) return

        viewModelScope.launch {
            if (state.editingEvent != null) {
                // Update existing event
                val updated = state.editingEvent.copy(
                    targetDate = state.addEventDate.toString(),
                    eventName = state.addEventName,
                    type = state.addEventType
                )
                attendanceRepository.updatePlannedDayOff(updated)
            } else {
                // Insert new event
                attendanceRepository.insertPlannedDayOff(
                    targetDate = state.addEventDate.toString(),
                    eventName = state.addEventName,
                    type = state.addEventType
                )
            }
            _uiState.update { it.copy(showAddEventDialog = false, editingEvent = null) }
            // Re-select the date to refresh the detail view
            selectDate(state.addEventDate)
        }
    }

    fun deleteEvent(event: PlannedDayOffEntity) {
        viewModelScope.launch {
            attendanceRepository.deletePlannedDayOff(event)
            // Refresh the selected date view
            _uiState.value.selectedDate?.let { selectDate(it) }
        }
    }
}
