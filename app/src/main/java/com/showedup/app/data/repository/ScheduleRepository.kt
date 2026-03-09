package com.showedup.app.data.repository

import com.showedup.app.crypto.CanonicalJson
import com.showedup.app.crypto.HashUtils
import com.showedup.app.data.dao.SubjectDao
import com.showedup.app.data.dao.TimetableDao
import com.showedup.app.data.dao.WeeklyScheduleDao
import com.showedup.app.data.entity.SubjectEntity
import com.showedup.app.data.entity.TimetableEntry
import com.showedup.app.data.entity.WeeklyScheduleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor(
    private val timetableDao: TimetableDao,
    private val weeklyScheduleDao: WeeklyScheduleDao,
    private val subjectDao: SubjectDao,
    private val hashUtils: HashUtils,
    private val canonicalJson: CanonicalJson
) {

    fun getAllClasses(): Flow<List<TimetableEntry>> = timetableDao.getAll()

    fun getActiveClasses(): Flow<List<TimetableEntry>> = timetableDao.getAllActive()

    fun getClassesByDay(dayOfWeek: DayOfWeek): Flow<List<TimetableEntry>> =
        timetableDao.getByDayOfWeek(dayOfWeek.value)

    suspend fun getActiveClassesByDaySnapshot(dayOfWeek: DayOfWeek): List<TimetableEntry> =
        timetableDao.getActiveByDayOfWeekSnapshot(dayOfWeek.value)

    suspend fun getClassById(id: Long): TimetableEntry? = timetableDao.getById(id)

    suspend fun addClass(entry: TimetableEntry): Long = timetableDao.insert(entry)

    suspend fun updateClass(entry: TimetableEntry) = timetableDao.update(entry)

    suspend fun deleteClass(entry: TimetableEntry) = timetableDao.delete(entry)

    // Subjects
    fun getAllSubjects(): Flow<List<SubjectEntity>> = subjectDao.getAll()

    suspend fun getSubjectById(id: Long): SubjectEntity? = subjectDao.getById(id)

    suspend fun addSubject(subject: SubjectEntity): Long = subjectDao.insert(subject)

    suspend fun updateSubject(subject: SubjectEntity) = subjectDao.update(subject)

    suspend fun deleteSubject(subject: SubjectEntity) = subjectDao.delete(subject)

    // Weekly Schedule
    fun getLatestWeeklySchedule(): Flow<WeeklyScheduleEntity?> =
        weeklyScheduleDao.getLatestFlow()

    suspend fun getLatestWeeklyScheduleSnapshot(): WeeklyScheduleEntity? =
        weeklyScheduleDao.getLatest()

    suspend fun setWeeklySchedule(
        activeDays: List<DayOfWeek>,
        effectiveFrom: String
    ): Long {
        val previous = weeklyScheduleDao.getLatest()
        val inactiveDays = DayOfWeek.entries.filter { it !in activeDays }

        val recordData = mapOf<String, Any?>(
            "activeDays" to activeDays.joinToString(",") { it.value.toString() },
            "inactiveDays" to inactiveDays.joinToString(",") { it.value.toString() },
            "effectiveFrom" to effectiveFrom,
            "createdAt" to System.currentTimeMillis()
        )
        val canonicalBytes = canonicalJson.toCanonicalBytes(recordData)
        val chainHash = hashUtils.computeChainHash(
            canonicalBytes,
            previous?.chainHash?.let { hashUtils.fromHex(it) }
        )

        val schedule = WeeklyScheduleEntity(
            activeDays = activeDays.joinToString(",") { it.value.toString() },
            inactiveDays = inactiveDays.joinToString(",") { it.value.toString() },
            effectiveFrom = effectiveFrom,
            chainHash = hashUtils.toHex(chainHash),
            previousChainHash = previous?.chainHash
        )
        return weeklyScheduleDao.insert(schedule)
    }
}
