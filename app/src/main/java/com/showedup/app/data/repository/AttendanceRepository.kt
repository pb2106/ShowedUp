package com.showedup.app.data.repository

import com.showedup.app.crypto.CanonicalJson
import com.showedup.app.crypto.HashUtils
import com.showedup.app.crypto.KeystoreManager
import com.showedup.app.data.dao.AttendanceDao
import com.showedup.app.data.dao.DayOffDao
import com.showedup.app.data.dao.PlannedDayOffDao
import com.showedup.app.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepository @Inject constructor(
    private val attendanceDao: AttendanceDao,
    private val dayOffDao: DayOffDao,
    private val plannedDayOffDao: PlannedDayOffDao,
    private val hashUtils: HashUtils,
    private val canonicalJson: CanonicalJson,
    private val keystoreManager: KeystoreManager
) {

    fun getAllRecords(): Flow<List<AttendanceRecordEntity>> = attendanceDao.getAll()

    fun getRecordsByDate(date: String): Flow<List<AttendanceRecordEntity>> =
        attendanceDao.getByDate(date)

    fun getRecordsByDateRange(start: String, end: String): Flow<List<AttendanceRecordEntity>> =
        attendanceDao.getByDateRange(start, end)

    fun searchRecords(query: String): Flow<List<AttendanceRecordEntity>> =
        attendanceDao.search(query)

    fun getAllDates(): Flow<List<String>> = attendanceDao.getAllDates()

    suspend fun countByDate(date: String): Int = attendanceDao.countByDate(date)

    suspend fun insertRecord(
        timetableEntryId: Long,
        courseName: String,
        date: String,
        gpsHash: String?,
        wifiHash: String?,
        bluetoothHash: String?,
        audioHash: String?,
        sensorHash: String?
    ): Long {
        val timestamp = System.currentTimeMillis()
        val previousRecord = attendanceDao.getLatest()

        // Build canonical data for hashing/signing
        val recordData = mapOf<String, Any?>(
            "timetableEntryId" to timetableEntryId,
            "courseName" to courseName,
            "timestamp" to timestamp,
            "date" to date,
            "gpsHash" to gpsHash,
            "wifiHash" to wifiHash,
            "bluetoothHash" to bluetoothHash,
            "audioHash" to audioHash,
            "sensorHash" to sensorHash
        )

        val canonicalBytes = canonicalJson.toCanonicalBytes(recordData)
        val canonicalString = canonicalJson.toCanonicalString(recordData)
        val chainHash = hashUtils.computeChainHash(
            canonicalBytes,
            previousRecord?.chainHash?.let { hashUtils.fromHex(it) }
        )

        val signature = try {
            keystoreManager.sign(canonicalBytes)
        } catch (e: Exception) {
            // If biometric auth is needed, this will throw — handle in caller
            throw e
        }

        val record = AttendanceRecordEntity(
            timetableEntryId = timetableEntryId,
            courseName = courseName,
            timestamp = timestamp,
            date = date,
            gpsHash = gpsHash,
            wifiHash = wifiHash,
            bluetoothHash = bluetoothHash,
            audioHash = audioHash,
            sensorHash = sensorHash,
            gpsAvailable = gpsHash != null,
            wifiAvailable = wifiHash != null,
            bluetoothAvailable = bluetoothHash != null,
            audioAvailable = audioHash != null,
            sensorAvailable = sensorHash != null,
            chainHash = hashUtils.toHex(chainHash),
            previousChainHash = previousRecord?.chainHash,
            signature = android.util.Base64.encodeToString(signature, android.util.Base64.NO_WRAP),
            canonicalJson = canonicalString
        )

        return attendanceDao.insert(record)
    }

    // Day Off operations
    fun getAllDayOffs(): Flow<List<DayOffRecordEntity>> = dayOffDao.getAll()

    suspend fun getDayOffsByDate(date: String): List<DayOffRecordEntity> =
        dayOffDao.getByDate(date)

    fun getDayOffsByDateRange(start: String, end: String): Flow<List<DayOffRecordEntity>> =
        dayOffDao.getByDateRange(start, end)

    suspend fun insertDayOff(
        type: DayOffType,
        date: String,
        reason: String,
        classesSuppressed: List<Long>
    ): Long {
        val previous = dayOffDao.getLatest()
        val recordData = mapOf<String, Any?>(
            "type" to type.name,
            "date" to date,
            "reason" to reason,
            "classesSuppressed" to classesSuppressed.joinToString(","),
            "declaredAt" to System.currentTimeMillis()
        )
        val canonicalBytes = canonicalJson.toCanonicalBytes(recordData)
        val chainHash = hashUtils.computeChainHash(
            canonicalBytes,
            previous?.chainHash?.let { hashUtils.fromHex(it) }
        )

        val record = DayOffRecordEntity(
            type = type,
            date = date,
            reason = reason,
            classesSuppressed = classesSuppressed.joinToString(","),
            chainHash = hashUtils.toHex(chainHash),
            previousChainHash = previous?.chainHash
        )
        return dayOffDao.insert(record)
    }

    // Planned Day Off operations
    fun getAllPlannedDayOffs(): Flow<List<PlannedDayOffEntity>> = plannedDayOffDao.getAll()
    fun getPendingPlannedDayOffs(): Flow<List<PlannedDayOffEntity>> = plannedDayOffDao.getPending()

    suspend fun insertPlannedDayOff(
        targetDate: String,
        eventName: String,
        type: DayOffType
    ): Long {
        val previous = plannedDayOffDao.getByDate(targetDate).lastOrNull()
        val recordData = mapOf<String, Any?>(
            "targetDate" to targetDate,
            "eventName" to eventName,
            "type" to type.name,
            "createdAt" to System.currentTimeMillis()
        )
        val canonicalBytes = canonicalJson.toCanonicalBytes(recordData)
        val chainHash = hashUtils.computeChainHash(
            canonicalBytes,
            previous?.chainHash?.let { hashUtils.fromHex(it) }
        )

        val record = PlannedDayOffEntity(
            targetDate = targetDate,
            eventName = eventName,
            type = type,
            chainHash = hashUtils.toHex(chainHash),
            previousChainHash = previous?.chainHash
        )
        return plannedDayOffDao.insert(record)
    }

    suspend fun updatePlannedDayOff(entity: PlannedDayOffEntity) =
        plannedDayOffDao.update(entity)

    suspend fun deletePlannedDayOff(entity: PlannedDayOffEntity) =
        plannedDayOffDao.delete(entity)
}
