package com.showedup.app.di

import android.content.Context
import androidx.room.Room
import com.showedup.app.data.ShowedUpDatabase
import com.showedup.app.data.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShowedUpDatabase {
        return Room.databaseBuilder(
            context,
            ShowedUpDatabase::class.java,
            "showedup_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides fun provideTimetableDao(db: ShowedUpDatabase): TimetableDao = db.timetableDao()
    @Provides fun provideAttendanceDao(db: ShowedUpDatabase): AttendanceDao = db.attendanceDao()
    @Provides fun provideDayOffDao(db: ShowedUpDatabase): DayOffDao = db.dayOffDao()
    @Provides fun providePlannedDayOffDao(db: ShowedUpDatabase): PlannedDayOffDao = db.plannedDayOffDao()
    @Provides fun provideWeeklyScheduleDao(db: ShowedUpDatabase): WeeklyScheduleDao = db.weeklyScheduleDao()
    @Provides fun provideSecurityEventDao(db: ShowedUpDatabase): SecurityEventDao = db.securityEventDao()
}
