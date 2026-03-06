package com.showedup.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.showedup.app.data.entity.AttendanceRecordEntity
import com.showedup.app.data.entity.TimetableEntry
import com.showedup.app.data.repository.AttendanceRepository
import com.showedup.app.data.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class HomeUiState(
    val todayClasses: List<TimetableEntry> = emptyList(),
    val todayRecords: List<AttendanceRecordEntity> = emptyList(),
    val attendedCount: Int = 0,
    val totalCount: Int = 0,
    val isLoading: Boolean = true,
    val greeting: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val today = LocalDate.now()
    private val todayStr = today.format(DateTimeFormatter.ISO_LOCAL_DATE)

    init {
        loadTodayData()
    }

    private fun loadTodayData() {
        viewModelScope.launch {
            val greeting = getGreeting()
            _uiState.update { it.copy(greeting = greeting) }

            // Get today's classes
            scheduleRepository.getClassesByDay(today.dayOfWeek)
                .combine(attendanceRepository.getRecordsByDate(todayStr)) { classes, records ->
                    HomeUiState(
                        todayClasses = classes,
                        todayRecords = records,
                        attendedCount = records.size,
                        totalCount = classes.size,
                        isLoading = false,
                        greeting = greeting
                    )
                }
                .collect { state -> _uiState.value = state }
        }
    }

    private fun getGreeting(): String {
        val hour = java.time.LocalTime.now().hour
        return when {
            hour < 12 -> "Good morning"
            hour < 17 -> "Good afternoon"
            else -> "Good evening"
        }
    }
}
