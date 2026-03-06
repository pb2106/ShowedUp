package com.showedup.app.ui.screens.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.showedup.app.data.entity.AttendanceRecordEntity
import com.showedup.app.data.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LogUiState(
    val records: List<AttendanceRecordEntity> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: LogFilter = LogFilter.ALL,
    val expandedRecordId: Long? = null,
    val isLoading: Boolean = true
)

enum class LogFilter(val label: String) {
    ALL("All"),
    TODAY("Today"),
    THIS_WEEK("This Week"),
    THIS_MONTH("This Month")
}

@HiltViewModel
class AttendanceLogViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LogUiState())
    val uiState: StateFlow<LogUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        loadRecords()
    }

    private fun loadRecords() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        attendanceRepository.getAllRecords()
                    } else {
                        attendanceRepository.searchRecords(query)
                    }
                }
                .collect { records ->
                    _uiState.update { it.copy(records = records, isLoading = false) }
                }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        _searchQuery.value = query
    }

    fun setFilter(filter: LogFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        // Reload with date range filtering
        viewModelScope.launch {
            val now = java.time.LocalDate.now()
            val (start, end) = when (filter) {
                LogFilter.ALL -> null to null
                LogFilter.TODAY -> now to now
                LogFilter.THIS_WEEK -> now.minusDays(now.dayOfWeek.value.toLong() - 1) to now
                LogFilter.THIS_MONTH -> now.withDayOfMonth(1) to now
            }

            if (start != null && end != null) {
                attendanceRepository.getRecordsByDateRange(start.toString(), end.toString())
                    .collect { records ->
                        _uiState.update { it.copy(records = records) }
                    }
            } else {
                attendanceRepository.getAllRecords()
                    .collect { records ->
                        _uiState.update { it.copy(records = records) }
                    }
            }
        }
    }

    fun toggleExpanded(recordId: Long) {
        _uiState.update {
            it.copy(expandedRecordId = if (it.expandedRecordId == recordId) null else recordId)
        }
    }
}
