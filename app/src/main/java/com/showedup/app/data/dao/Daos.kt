package com.showedup.app.data.dao

import androidx.room.*
import com.showedup.app.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: TimetableEntry): Long

    @Update
    suspend fun update(entry: TimetableEntry)

    @Delete
    suspend fun delete(entry: TimetableEntry)

    @Query("SELECT * FROM timetable_entries WHERE isActive = 1 ORDER BY dayOfWeek, startTimeMinutes")
    fun getAllActive(): Flow<List<TimetableEntry>>

    @Query("SELECT * FROM timetable_entries WHERE isActive = 1 AND dayOfWeek = :dayOfWeek ORDER BY startTimeMinutes")
    fun getByDayOfWeek(dayOfWeek: Int): Flow<List<TimetableEntry>>

    @Query("SELECT * FROM timetable_entries WHERE id = :id")
    suspend fun getById(id: Long): TimetableEntry?

    @Query("SELECT * FROM timetable_entries ORDER BY dayOfWeek, startTimeMinutes")
    fun getAll(): Flow<List<TimetableEntry>>
}

@Dao
interface AttendanceDao {
    @Insert
    suspend fun insert(record: AttendanceRecordEntity): Long

    @Query("SELECT * FROM attendance_records ORDER BY timestamp DESC")
    fun getAll(): Flow<List<AttendanceRecordEntity>>

    @Query("SELECT * FROM attendance_records WHERE date = :date ORDER BY timestamp DESC")
    fun getByDate(date: String): Flow<List<AttendanceRecordEntity>>

    @Query("SELECT * FROM attendance_records WHERE date BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getByDateRange(startDate: String, endDate: String): Flow<List<AttendanceRecordEntity>>

    @Query("SELECT * FROM attendance_records WHERE courseName LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun search(query: String): Flow<List<AttendanceRecordEntity>>

    @Query("SELECT * FROM attendance_records ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatest(): AttendanceRecordEntity?

    @Query("SELECT COUNT(*) FROM attendance_records WHERE date = :date")
    suspend fun countByDate(date: String): Int

    @Query("SELECT DISTINCT date FROM attendance_records ORDER BY date DESC")
    fun getAllDates(): Flow<List<String>>
}

@Dao
interface DayOffDao {
    @Insert
    suspend fun insert(record: DayOffRecordEntity): Long

    @Query("SELECT * FROM day_off_records ORDER BY date DESC")
    fun getAll(): Flow<List<DayOffRecordEntity>>

    @Query("SELECT * FROM day_off_records WHERE date = :date")
    suspend fun getByDate(date: String): List<DayOffRecordEntity>

    @Query("SELECT * FROM day_off_records WHERE date BETWEEN :startDate AND :endDate")
    fun getByDateRange(startDate: String, endDate: String): Flow<List<DayOffRecordEntity>>

    @Query("SELECT * FROM day_off_records ORDER BY declaredAt DESC LIMIT 1")
    suspend fun getLatest(): DayOffRecordEntity?
}

@Dao
interface PlannedDayOffDao {
    @Insert
    suspend fun insert(record: PlannedDayOffEntity): Long

    @Update
    suspend fun update(record: PlannedDayOffEntity)

    @Delete
    suspend fun delete(record: PlannedDayOffEntity)

    @Query("SELECT * FROM planned_day_offs WHERE status = 'PENDING' ORDER BY targetDate")
    fun getPending(): Flow<List<PlannedDayOffEntity>>

    @Query("SELECT * FROM planned_day_offs WHERE targetDate = :date")
    suspend fun getByDate(date: String): List<PlannedDayOffEntity>

    @Query("SELECT * FROM planned_day_offs ORDER BY targetDate DESC")
    fun getAll(): Flow<List<PlannedDayOffEntity>>
}

@Dao
interface WeeklyScheduleDao {
    @Insert
    suspend fun insert(schedule: WeeklyScheduleEntity): Long

    @Query("SELECT * FROM weekly_schedules ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatest(): WeeklyScheduleEntity?

    @Query("SELECT * FROM weekly_schedules ORDER BY createdAt DESC LIMIT 1")
    fun getLatestFlow(): Flow<WeeklyScheduleEntity?>

    @Query("SELECT * FROM weekly_schedules ORDER BY createdAt DESC")
    fun getAll(): Flow<List<WeeklyScheduleEntity>>
}

@Dao
interface SecurityEventDao {
    @Insert
    suspend fun insert(event: SecurityEventEntity): Long

    @Query("SELECT * FROM security_events ORDER BY timestamp DESC")
    fun getAll(): Flow<List<SecurityEventEntity>>

    @Query("SELECT * FROM security_events WHERE eventType = :type ORDER BY timestamp DESC")
    fun getByType(type: String): Flow<List<SecurityEventEntity>>

    @Query("SELECT * FROM security_events ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatest(): SecurityEventEntity?

    @Query("SELECT COUNT(*) FROM security_events WHERE eventType = 'BIOMETRIC_FAILURE' AND timestamp > :since")
    suspend fun countBiometricFailuresSince(since: Long): Int
}
