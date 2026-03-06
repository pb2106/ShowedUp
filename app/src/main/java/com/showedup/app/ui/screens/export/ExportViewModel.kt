package com.showedup.app.ui.screens.export

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.showedup.app.data.entity.AttendanceRecordEntity
import com.showedup.app.data.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class ExportUiState(
    val startDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate = LocalDate.now(),
    val recordCount: Int = 0,
    val isGenerating: Boolean = false,
    val isGenerated: Boolean = false,
    val pdfPath: String? = null
)

@HiltViewModel
class ExportViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportUiState())
    val uiState: StateFlow<ExportUiState> = _uiState.asStateFlow()

    init {
        updateRecordCount()
    }

    fun setStartDate(date: LocalDate) {
        _uiState.update { it.copy(startDate = date) }
        updateRecordCount()
    }

    fun setEndDate(date: LocalDate) {
        _uiState.update { it.copy(endDate = date) }
        updateRecordCount()
    }

    private fun updateRecordCount() {
        viewModelScope.launch {
            val state = _uiState.value
            attendanceRepository.getRecordsByDateRange(
                state.startDate.toString(),
                state.endDate.toString()
            ).collect { records ->
                _uiState.update { it.copy(recordCount = records.size) }
            }
        }
    }

    fun generatePdf() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true) }

            val state = _uiState.value
            val records = mutableListOf<AttendanceRecordEntity>()

            attendanceRepository.getRecordsByDateRange(
                state.startDate.toString(),
                state.endDate.toString()
            ).first().let { records.addAll(it) }

            val file = createPdf(records, state.startDate, state.endDate)

            _uiState.update {
                it.copy(
                    isGenerating = false,
                    isGenerated = true,
                    pdfPath = file.absolutePath
                )
            }
        }
    }

    private fun createPdf(
        records: List<AttendanceRecordEntity>,
        startDate: LocalDate,
        endDate: LocalDate
    ): File {
        val document = PdfDocument()
        val pageWidth = 595 // A4
        val pageHeight = 842

        var pageNum = 1
        var yPos = 80f

        fun newPage(): PdfDocument.Page {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum++).create()
            return document.startPage(pageInfo)
        }

        var page = newPage()
        var canvas = page.canvas

        val titlePaint = Paint().apply {
            textSize = 24f
            isFakeBoldText = true
            color = android.graphics.Color.parseColor("#111827")
        }
        val headerPaint = Paint().apply {
            textSize = 14f
            isFakeBoldText = true
            color = android.graphics.Color.parseColor("#374151")
        }
        val bodyPaint = Paint().apply {
            textSize = 12f
            color = android.graphics.Color.parseColor("#4B5563")
        }
        val accentPaint = Paint().apply {
            textSize = 12f
            color = android.graphics.Color.parseColor("#10B981")
        }

        // Title
        canvas.drawText("Attendance Report", 40f, yPos, titlePaint)
        yPos += 30f
        val dateRange = "${startDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))} – ${endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}"
        canvas.drawText(dateRange, 40f, yPos, bodyPaint)
        yPos += 20f
        canvas.drawText("Total records: ${records.size}", 40f, yPos, bodyPaint)
        yPos += 40f

        // Table header
        canvas.drawText("Date", 40f, yPos, headerPaint)
        canvas.drawText("Course", 140f, yPos, headerPaint)
        canvas.drawText("Time", 340f, yPos, headerPaint)
        canvas.drawText("Signals", 440f, yPos, headerPaint)
        yPos += 20f

        // Records
        for (record in records) {
            if (yPos > pageHeight - 60) {
                document.finishPage(page)
                page = newPage()
                canvas = page.canvas
                yPos = 60f
            }

            val time = java.time.Instant.ofEpochMilli(record.timestamp)
                .atZone(java.time.ZoneId.systemDefault())

            canvas.drawText(record.date, 40f, yPos, bodyPaint)
            canvas.drawText(
                record.courseName.take(25),
                140f, yPos, bodyPaint
            )
            canvas.drawText(
                "%02d:%02d".format(time.hour, time.minute),
                340f, yPos, bodyPaint
            )

            val signalCount = listOf(
                record.gpsAvailable, record.wifiAvailable,
                record.bluetoothAvailable, record.audioAvailable,
                record.sensorAvailable
            ).count { it }
            canvas.drawText("$signalCount/5", 440f, yPos, accentPaint)

            yPos += 18f
        }

        document.finishPage(page)

        // Save
        val exportDir = File(context.cacheDir, "exports")
        exportDir.mkdirs()
        val file = File(exportDir, "attendance_report_${System.currentTimeMillis()}.pdf")
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()

        return file
    }

    fun sharePdf() {
        val path = _uiState.value.pdfPath ?: return
        val file = File(path)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Share Report").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}
