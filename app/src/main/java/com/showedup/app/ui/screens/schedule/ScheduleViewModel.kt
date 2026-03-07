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
    val editingEntry: TimetableEntry? = null,
    // For timetable grid — which cell was tapped (day + slot index)
    val tappedDay: DayOfWeek? = null,
    val tappedSlotIndex: Int? = null,
    // Time slots that define the columns
    val timeSlots: List<TimeSlot> = defaultTimeSlots()
)

/**
 * Represents one column in the timetable grid.
 */
data class TimeSlot(
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int
) {
    val startTimeMinutes: Int get() = startHour * 60 + startMinute
    val endTimeMinutes: Int get() = endHour * 60 + endMinute

    val label: String
        get() {
            val sh = if (startHour > 12) startHour - 12 else if (startHour == 0) 12 else startHour
            val sp = if (startHour >= 12) "pm" else "am"
            return if (startMinute == 0) "$sh $sp"
            else "%d:%02d $sp".format(sh, startMinute)
        }
}

fun defaultTimeSlots(): List<TimeSlot> = listOf(
    TimeSlot(8, 45, 9, 45),
    TimeSlot(9, 45, 10, 45),
    TimeSlot(10, 45, 11, 0),   // Short break slot
    TimeSlot(11, 0, 12, 0),
    TimeSlot(12, 0, 13, 0),
    TimeSlot(13, 0, 14, 0),    // Lunch break slot
    TimeSlot(14, 0, 14, 50),
    TimeSlot(14, 50, 15, 40),
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

                    _uiState.value.copy(
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

    /**
     * Called when tapping an empty cell in the table grid.
     * Opens the add dialog pre-filled with the tapped day and time slot.
     */
    fun showAddDialogForCell(day: DayOfWeek, slotIndex: Int) {
        _uiState.update {
            it.copy(
                showAddDialog = true,
                editingEntry = null,
                tappedDay = day,
                tappedSlotIndex = slotIndex
            )
        }
    }

    fun showAddDialog() {
        _uiState.update { it.copy(showAddDialog = true, editingEntry = null, tappedDay = null, tappedSlotIndex = null) }
    }

    fun showEditDialog(entry: TimetableEntry) {
        _uiState.update { it.copy(showAddDialog = true, editingEntry = entry) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showAddDialog = false, editingEntry = null, tappedDay = null, tappedSlotIndex = null) }
    }

    fun saveClass(
        courseName: String,
        courseCode: String,
        daysOfWeek: Set<DayOfWeek>,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        instructor: String
    ) {
        viewModelScope.launch {
            if (_uiState.value.editingEntry != null) {
                val updated = _uiState.value.editingEntry!!.copy(
                    courseName = courseName,
                    courseCode = courseCode,
                    dayOfWeek = daysOfWeek.first(),
                    startTimeMinutes = startHour * 60 + startMinute,
                    endTimeMinutes = endHour * 60 + endMinute,
                    instructor = instructor
                )
                scheduleRepository.updateClass(updated)
            } else {
                for (day in daysOfWeek.sorted()) {
                    val entry = TimetableEntry(
                        courseName = courseName,
                        courseCode = courseCode,
                        dayOfWeek = day,
                        startTimeMinutes = startHour * 60 + startMinute,
                        endTimeMinutes = endHour * 60 + endMinute,
                        instructor = instructor
                    )
                    scheduleRepository.addClass(entry)
                }
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
