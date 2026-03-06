package com.showedup.app.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.showedup.app.data.entity.AttendanceRecordEntity
import com.showedup.app.data.entity.DayOffRecordEntity
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
    val selectedDateRecords: List<AttendanceRecordEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        loadDates()
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

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        viewModelScope.launch {
            attendanceRepository.getRecordsByDate(date.toString())
                .collect { records ->
                    _uiState.update { it.copy(selectedDateRecords = records) }
                }
        }
    }

    fun changeMonth(yearMonth: YearMonth) {
        _uiState.update { it.copy(currentMonth = yearMonth) }
    }
}
