package com.showedup.app.data.entity

import androidx.room.*
import java.time.DayOfWeek
import java.time.LocalTime

/**
 * Timetable entry representing a recurring class.
 */
@Entity(tableName = "timetable_entries")
data class TimetableEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val courseName: String,
    val courseCode: String = "",
    @TypeConverters(Converters::class)
    val dayOfWeek: DayOfWeek,
    val startTimeMinutes: Int, // minutes from midnight
    val endTimeMinutes: Int,
    val room: String = "",
    val instructor: String = "",
    val locationLat: Double? = null,
    val locationLng: Double? = null,
    val geofenceRadius: Float = 500f, // meters, default per PRD
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Attendance record with signal hashes, chain hash, and signature.
 */
@Entity(tableName = "attendance_records")
data class AttendanceRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timetableEntryId: Long,
    val courseName: String,
    val timestamp: Long,
    val date: String, // ISO date YYYY-MM-DD
    // Signal hashes (raw values never stored)
    val gpsHash: String?,
    val wifiHash: String?,
    val bluetoothHash: String?,
    val audioHash: String?,
    val sensorHash: String?,
    // Signal availability flags
    val gpsAvailable: Boolean = false,
    val wifiAvailable: Boolean = false,
    val bluetoothAvailable: Boolean = false,
    val audioAvailable: Boolean = false,
    val sensorAvailable: Boolean = false,
    // Crypto chain
    val chainHash: String,
    val previousChainHash: String?,
    val signature: String, // base64-encoded ECDSA signature
    val canonicalJson: String // deterministic JSON used for signing
)

/**
 * Day-off record (sick, personal, holiday, event).
 */
@Entity(tableName = "day_off_records")
data class DayOffRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: DayOffType,
    val date: String, // ISO date
    val reason: String = "",
    val declaredAt: Long = System.currentTimeMillis(),
    val classesSuppressed: String = "", // comma-separated timetable entry IDs
    val chainHash: String,
    val previousChainHash: String?
)

enum class DayOffType {
    SICK, PERSONAL, HOLIDAY, EVENT
}

/**
 * Planned day-off / event.
 */
@Entity(tableName = "planned_day_offs")
data class PlannedDayOffEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val targetDate: String, // ISO date
    val eventName: String,
    val type: DayOffType = DayOffType.EVENT,
    val status: PlannedDayOffStatus = PlannedDayOffStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val chainHash: String,
    val previousChainHash: String?
)

enum class PlannedDayOffStatus {
    PENDING, CONFIRMED, CANCELLED
}

/**
 * Weekly schedule configuration (active/inactive days).
 */
@Entity(tableName = "weekly_schedules")
data class WeeklyScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val activeDays: String, // comma-separated DayOfWeek ordinals
    val inactiveDays: String,
    val effectiveFrom: String, // ISO date
    val effectiveTo: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val chainHash: String,
    val previousChainHash: String?
)

/**
 * Security event log entry.
 */
@Entity(tableName = "security_events")
data class SecurityEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventType: SecurityEventType,
    val timestamp: Long = System.currentTimeMillis(),
    val data: String = "", // JSON metadata
    val chainHash: String,
    val previousChainHash: String?
)

enum class SecurityEventType {
    BIOMETRIC_SUCCESS,
    BIOMETRIC_FAILURE,
    BIOMETRIC_LOCKOUT,
    ROOT_DETECTED,
    EMULATOR_DETECTED,
    APK_TAMPER_DETECTED,
    KEY_GENERATED,
    APP_OPENED,
    SIGNAL_COLLECTION_STARTED,
    SIGNAL_COLLECTION_COMPLETED,
    SIGNAL_COLLECTION_FAILED
}

/**
 * Room type converters.
 */
class Converters {
    @TypeConverter
    fun fromDayOfWeek(day: DayOfWeek): Int = day.value

    @TypeConverter
    fun toDayOfWeek(value: Int): DayOfWeek = DayOfWeek.of(value)

    @TypeConverter
    fun fromDayOffType(type: DayOffType): String = type.name

    @TypeConverter
    fun toDayOffType(value: String): DayOffType = DayOffType.valueOf(value)

    @TypeConverter
    fun fromPlannedDayOffStatus(status: PlannedDayOffStatus): String = status.name

    @TypeConverter
    fun toPlannedDayOffStatus(value: String): PlannedDayOffStatus = PlannedDayOffStatus.valueOf(value)

    @TypeConverter
    fun fromSecurityEventType(type: SecurityEventType): String = type.name

    @TypeConverter
    fun toSecurityEventType(value: String): SecurityEventType = SecurityEventType.valueOf(value)
}
