package com.showedup.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.showedup.app.data.dao.*
import com.showedup.app.data.entity.*

@Database(
    entities = [
        TimetableEntry::class,
        AttendanceRecordEntity::class,
        DayOffRecordEntity::class,
        PlannedDayOffEntity::class,
        WeeklyScheduleEntity::class,
        SecurityEventEntity::class,
        SubjectEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ShowedUpDatabase : RoomDatabase() {
    abstract fun timetableDao(): TimetableDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun dayOffDao(): DayOffDao
    abstract fun plannedDayOffDao(): PlannedDayOffDao
    abstract fun weeklyScheduleDao(): WeeklyScheduleDao
    abstract fun securityEventDao(): SecurityEventDao
    abstract fun subjectDao(): SubjectDao
}
