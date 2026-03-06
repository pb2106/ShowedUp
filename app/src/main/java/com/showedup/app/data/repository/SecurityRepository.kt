package com.showedup.app.data.repository

import com.showedup.app.crypto.CanonicalJson
import com.showedup.app.crypto.HashUtils
import com.showedup.app.data.dao.SecurityEventDao
import com.showedup.app.data.entity.SecurityEventEntity
import com.showedup.app.data.entity.SecurityEventType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityRepository @Inject constructor(
    private val securityEventDao: SecurityEventDao,
    private val hashUtils: HashUtils,
    private val canonicalJson: CanonicalJson
) {

    fun getAllEvents(): Flow<List<SecurityEventEntity>> = securityEventDao.getAll()

    suspend fun logEvent(type: SecurityEventType, data: String = ""): Long {
        val previous = securityEventDao.getLatest()
        val timestamp = System.currentTimeMillis()

        val recordData = mapOf<String, Any?>(
            "eventType" to type.name,
            "timestamp" to timestamp,
            "data" to data
        )
        val canonicalBytes = canonicalJson.toCanonicalBytes(recordData)
        val chainHash = hashUtils.computeChainHash(
            canonicalBytes,
            previous?.chainHash?.let { hashUtils.fromHex(it) }
        )

        val event = SecurityEventEntity(
            eventType = type,
            timestamp = timestamp,
            data = data,
            chainHash = hashUtils.toHex(chainHash),
            previousChainHash = previous?.chainHash
        )
        return securityEventDao.insert(event)
    }

    suspend fun countRecentBiometricFailures(windowMs: Long = 30 * 60 * 1000): Int {
        val since = System.currentTimeMillis() - windowMs
        return securityEventDao.countBiometricFailuresSince(since)
    }
}
