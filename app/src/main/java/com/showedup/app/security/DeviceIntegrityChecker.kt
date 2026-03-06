package com.showedup.app.security

import android.os.Build
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Device integrity checks: root detection, emulator detection, APK signature verification.
 */
@Singleton
class DeviceIntegrityChecker @Inject constructor(
    @ApplicationContext private val context: Context
) {

    data class IntegrityResult(
        val isRooted: Boolean = false,
        val isEmulator: Boolean = false,
        val isApkTampered: Boolean = false,
        val details: List<String> = emptyList()
    ) {
        val isCompromised: Boolean get() = isRooted || isEmulator || isApkTampered
    }

    fun check(allowEmulator: Boolean = false): IntegrityResult {
        val details = mutableListOf<String>()
        val isRooted = checkRoot(details)
        val isEmulator = checkEmulator(details)
        val isApkTampered = checkApkSignature(details)

        return IntegrityResult(
            isRooted = isRooted,
            isEmulator = if (allowEmulator) false else isEmulator,
            isApkTampered = isApkTampered,
            details = details
        )
    }

    private fun checkRoot(details: MutableList<String>): Boolean {
        var rooted = false

        // Check for su binary
        val suPaths = listOf(
            "/system/bin/su", "/system/xbin/su", "/sbin/su",
            "/data/local/xbin/su", "/data/local/bin/su",
            "/system/sd/xbin/su", "/system/bin/failsafe/su",
            "/data/local/su"
        )
        for (path in suPaths) {
            if (java.io.File(path).exists()) {
                details.add("su binary found: $path")
                rooted = true
            }
        }

        // Check for Magisk
        val magiskPaths = listOf(
            "/sbin/.magisk", "/data/adb/magisk",
            "/cache/.disable_magisk"
        )
        for (path in magiskPaths) {
            if (java.io.File(path).exists()) {
                details.add("Magisk indicator found: $path")
                rooted = true
            }
        }

        // Check for busybox
        val busyboxPaths = listOf(
            "/system/xbin/busybox", "/system/bin/busybox",
            "/sbin/busybox"
        )
        for (path in busyboxPaths) {
            if (java.io.File(path).exists()) {
                details.add("busybox found: $path")
                rooted = true
            }
        }

        // Check system partition writability
        try {
            val mount = Runtime.getRuntime().exec("mount")
            val output = mount.inputStream.bufferedReader().readText()
            if (output.contains("/system") && output.contains("rw")) {
                details.add("/system is mounted read-write")
                rooted = true
            }
        } catch (_: Exception) {}

        return rooted
    }

    private fun checkEmulator(details: MutableList<String>): Boolean {
        val indicators = mutableListOf<String>()

        if (Build.FINGERPRINT.contains("generic") || Build.FINGERPRINT.contains("emulator")) {
            indicators.add("fingerprint: ${Build.FINGERPRINT}")
        }
        if (Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK")) {
            indicators.add("model: ${Build.MODEL}")
        }
        if (Build.MANUFACTURER.contains("Genymotion")) {
            indicators.add("manufacturer: ${Build.MANUFACTURER}")
        }
        if (Build.HARDWARE.contains("goldfish") || Build.HARDWARE.contains("ranchu")) {
            indicators.add("hardware: ${Build.HARDWARE}")
        }
        if (Build.PRODUCT.contains("sdk") || Build.PRODUCT.contains("emulator")) {
            indicators.add("product: ${Build.PRODUCT}")
        }

        if (indicators.isNotEmpty()) {
            details.addAll(indicators.map { "Emulator indicator: $it" })
            return true
        }
        return false
    }

    @Suppress("DEPRECATION")
    private fun checkApkSignature(details: MutableList<String>): Boolean {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                android.content.pm.PackageManager.GET_SIGNATURES
            )
            val signatures = packageInfo.signatures
            if (signatures.isNullOrEmpty()) {
                details.add("No APK signatures found")
                true
            } else {
                false // Signature exists — in production compare against known hash
            }
        } catch (e: Exception) {
            details.add("APK signature check failed: ${e.message}")
            true
        }
    }
}
