package com.showedup.app.data

import android.content.Context
import com.showedup.app.crypto.AesGcmCipher
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Append-only encrypted file for attendance records.
 * Each record is individually encrypted with AES-256-GCM.
 * Files stored at: /data/data/com.showedup.app/files/attendance/YYYY/MM/courseId_records.enc
 * File permissions: 0600 (owner read/write only).
 */
@Singleton
class EncryptedLogFile @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cipher: AesGcmCipher
) {

    companion object {
        private const val BASE_DIR = "attendance"
        private const val RECORD_SEPARATOR = "\n---RECORD---\n"
    }

    /**
     * Appends an encrypted record to the log file.
     */
    fun appendRecord(
        year: String,
        month: String,
        courseId: String,
        recordJson: String,
        passphrase: CharArray
    ) {
        val file = getOrCreateFile(year, month, courseId)
        val encrypted = cipher.encrypt(recordJson.toByteArray(Charsets.UTF_8), passphrase)
        val encoded = android.util.Base64.encodeToString(encrypted, android.util.Base64.NO_WRAP)

        file.appendText(encoded + RECORD_SEPARATOR)
        setFilePermissions(file)
    }

    /**
     * Reads and decrypts all records from a log file.
     */
    fun readRecords(
        year: String,
        month: String,
        courseId: String,
        passphrase: CharArray
    ): List<String> {
        val file = getFile(year, month, courseId) ?: return emptyList()
        if (!file.exists()) return emptyList()

        val content = file.readText()
        return content.split(RECORD_SEPARATOR)
            .filter { it.isNotBlank() }
            .map { encoded ->
                val encrypted = android.util.Base64.decode(encoded.trim(), android.util.Base64.NO_WRAP)
                String(cipher.decrypt(encrypted, passphrase), Charsets.UTF_8)
            }
    }

    private fun getOrCreateFile(year: String, month: String, courseId: String): File {
        val dir = File(context.filesDir, "$BASE_DIR/$year/$month")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, "${courseId}_records.enc")
        if (!file.exists()) {
            file.createNewFile()
            setFilePermissions(file)
        }
        return file
    }

    private fun getFile(year: String, month: String, courseId: String): File? {
        return File(context.filesDir, "$BASE_DIR/$year/$month/${courseId}_records.enc")
    }

    private fun setFilePermissions(file: File) {
        file.setReadable(false, false)
        file.setWritable(false, false)
        file.setExecutable(false, false)
        file.setReadable(true, true)  // owner only
        file.setWritable(true, true)  // owner only
    }
}
