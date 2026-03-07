package com.showedup.app.ui.screens.schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.showedup.app.data.entity.TimetableEntry
import com.showedup.app.data.entity.WeeklyScheduleEntity
import com.showedup.app.data.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
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
    val tappedDay: DayOfWeek? = null,
    val tappedSlotIndex: Int? = null,
    val timeSlots: List<TimeSlot> = defaultTimeSlots(),
    val showSlotEditor: Boolean = false,
    val editingSlotIndex: Int? = null
)

/**
 * Represents one column in the timetable grid.
 */
data class TimeSlot(
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val isBreak: Boolean = false,
    val breakLabel: String = ""
) {
    val startTimeMinutes: Int get() = startHour * 60 + startMinute
    val endTimeMinutes: Int get() = endHour * 60 + endMinute

    val label: String
        get() {
            if (isBreak) return breakLabel.ifBlank { "Break" }
            val sh = if (startHour > 12) startHour - 12 else if (startHour == 0) 12 else startHour
            val sp = if (startHour >= 12) "PM" else "AM"
            return if (startMinute == 0) "$sh $sp"
            else "%d:%02d %s".format(sh, startMinute, sp)
        }

    /**
     * Full range display in 12h format (e.g. "8:45 AM – 9:45 AM")
     */
    val rangeLabel: String
        get() {
            fun fmt(h: Int, m: Int): String {
                val h12 = if (h > 12) h - 12 else if (h == 0) 12 else h
                val ampm = if (h >= 12) "PM" else "AM"
                return if (m == 0) "$h12 $ampm" else "%d:%02d %s".format(h12, m, ampm)
            }
            return "${fmt(startHour, startMinute)} – ${fmt(endHour, endMinute)}"
        }
}



fun defaultTimeSlots(): List<TimeSlot> = listOf(
    TimeSlot(8, 45, 9, 45),
    TimeSlot(9, 45, 10, 45),
    TimeSlot(10, 45, 11, 0, isBreak = true, breakLabel = "Short\nBreak"),
    TimeSlot(11, 0, 12, 0),
    TimeSlot(12, 0, 13, 0),
    TimeSlot(13, 0, 14, 0, isBreak = true, breakLabel = "Lunch\nBreak"),
    TimeSlot(14, 0, 14, 50),
    TimeSlot(14, 50, 15, 40),
)

private const val PREFS_NAME = "showedup_prefs"
private const val KEY_TIME_SLOTS = "time_slots"

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    init {
        loadTimeSlots()
        loadSchedule()
    }

    private fun loadTimeSlots() {
        val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonStr = prefs.getString(KEY_TIME_SLOTS, null)
        if (jsonStr != null) {
            try {
                val arr = JSONArray(jsonStr)
                val slots = (0 until arr.length()).map { i ->
                    val o = arr.getJSONObject(i)
                    TimeSlot(
                        startHour = o.getInt("sh"),
                        startMinute = o.getInt("sm"),
                        endHour = o.getInt("eh"),
                        endMinute = o.getInt("em"),
                        isBreak = o.optBoolean("isBreak", false),
                        breakLabel = o.optString("breakLabel", "")
                    )
                }
                _uiState.update { it.copy(timeSlots = slots) }
            } catch (_: Exception) { /* use defaults */ }
        }
    }

    private fun saveTimeSlots(slots: List<TimeSlot>) {
        val arr = JSONArray()
        slots.forEach { s ->
            arr.put(JSONObject().apply {
                put("sh", s.startHour)
                put("sm", s.startMinute)
                put("eh", s.endHour)
                put("em", s.endMinute)
                put("isBreak", s.isBreak)
                put("breakLabel", s.breakLabel)
            })
        }
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_TIME_SLOTS, arr.toString()).apply()
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

    // --- Slot editor ---
    fun showSlotEditor(slotIndex: Int? = null) {
        _uiState.update { it.copy(showSlotEditor = true, editingSlotIndex = slotIndex) }
    }

    fun dismissSlotEditor() {
        _uiState.update { it.copy(showSlotEditor = false, editingSlotIndex = null) }
    }

    fun addSlot(slot: TimeSlot) {
        val slots = _uiState.value.timeSlots.toMutableList()
        slots.add(slot)
        slots.sortBy { it.startTimeMinutes }
        _uiState.update { it.copy(timeSlots = slots) }
        saveTimeSlots(slots)
        dismissSlotEditor()
    }

    fun updateSlot(index: Int, slot: TimeSlot) {
        val slots = _uiState.value.timeSlots.toMutableList()
        if (index in slots.indices) {
            slots[index] = slot
            slots.sortBy { it.startTimeMinutes }
            _uiState.update { it.copy(timeSlots = slots) }
            saveTimeSlots(slots)
        }
        dismissSlotEditor()
    }

    fun deleteSlot(index: Int) {
        val slots = _uiState.value.timeSlots.toMutableList()
        if (index in slots.indices) {
            slots.removeAt(index)
            _uiState.update { it.copy(timeSlots = slots) }
            saveTimeSlots(slots)
        }
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
