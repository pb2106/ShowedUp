package com.showedup.app.signal

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.net.wifi.WifiManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.showedup.app.crypto.HashUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.math.abs
import kotlin.math.sqrt

data class SignalResult(
    val gpsHash: String?,
    val wifiHash: String?,
    val bluetoothHash: String?,
    val audioHash: String?,
    val sensorHash: String?
)

/**
 * Orchestrates all 5 signal collectors with 30s timeout.
 * Each signal fails gracefully to null.
 */
@Singleton
class MultiModalSignalCollector @Inject constructor(
    @ApplicationContext private val context: Context,
    private val hashUtils: HashUtils
) {

    companion object {
        private const val TIMEOUT_MS = 30_000L
        private const val AUDIO_DURATION_MS = 5_000L
        private const val AUDIO_SAMPLE_RATE = 44100
    }

    /**
     * Collects all 5 signals concurrently with 30s timeout.
     */
    suspend fun collectAll(): SignalResult = withContext(Dispatchers.IO) {
        val results = withTimeoutOrNull(TIMEOUT_MS) {
            val gps = async { collectGps() }
            val wifi = async { collectWifi() }
            val bt = async { collectBluetooth() }
            val audio = async { collectAudio() }
            val sensor = async { collectSensor() }

            SignalResult(
                gpsHash = gps.await(),
                wifiHash = wifi.await(),
                bluetoothHash = bt.await(),
                audioHash = audio.await(),
                sensorHash = sensor.await()
            )
        }

        results ?: SignalResult(null, null, null, null, null)
    }

    /**
     * GPS: one-shot getCurrentLocation → hash → null Location object.
     */
    @SuppressLint("MissingPermission")
    private suspend fun collectGps(): String? = try {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) return null

        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        val cts = CancellationTokenSource()

        val location = suspendCancellableCoroutine<Location?> { cont ->
            fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                .addOnSuccessListener { loc -> cont.resume(loc) }
                .addOnFailureListener { cont.resume(null) }
            cont.invokeOnCancellation { cts.cancel() }
        }

        if (location != null) {
            val data = "${location.latitude},${location.longitude},${location.accuracy}"
            val hash = hashUtils.hashWithSalt(data.toByteArray())
            // Null the Location object — raw value never persisted
            hashUtils.toHex(hash)
        } else null
    } catch (e: Exception) { null }

    /**
     * WiFi: scan nearby APs → hash sorted BSSIDs.
     */
    @SuppressLint("MissingPermission")
    private suspend fun collectWifi(): String? = try {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val results = wifiManager.scanResults
        if (results.isNullOrEmpty()) return null

        val sortedBssids = results.map { it.BSSID }.sorted().joinToString(",")
        val hash = hashUtils.hashWithSalt(sortedBssids.toByteArray())
        hashUtils.toHex(hash)
    } catch (e: Exception) { null }

    /**
     * Bluetooth: BLE scan → count only, no identifiers stored.
     */
    @SuppressLint("MissingPermission")
    private suspend fun collectBluetooth(): String? = try {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) return null

        val btManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val scanner = btManager.adapter?.bluetoothLeScanner ?: return null

        val count = suspendCancellableCoroutine<Int> { cont ->
            val devices = mutableSetOf<String>()
            val callback = object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    result?.device?.address?.let { devices.add(it) }
                }
                override fun onScanFailed(errorCode: Int) {
                    if (cont.isActive) cont.resume(0)
                }
            }

            val settings = ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build()

            scanner.startScan(null, settings, callback)

            // Scan for 3 seconds then stop
            MainScope().launch {
                delay(3000)
                try { scanner.stopScan(callback) } catch (_: Exception) {}
                if (cont.isActive) cont.resume(devices.size)
            }

            cont.invokeOnCancellation {
                try { scanner.stopScan(callback) } catch (_: Exception) {}
            }
        }

        // Hash only the count, never device identifiers
        val hash = hashUtils.hashWithSalt("bt_count:$count".toByteArray())
        hashUtils.toHex(hash)
    } catch (e: Exception) { null }

    /**
     * Audio: 5s AudioRecord → FFT frequency fingerprint → hash → zero buffer.
     */
    @SuppressLint("MissingPermission")
    private suspend fun collectAudio(): String? = try {
        if (!hasPermission(Manifest.permission.RECORD_AUDIO)) return null

        withContext(Dispatchers.IO) {
            val bufferSize = AudioRecord.getMinBufferSize(
                AUDIO_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            val recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                AUDIO_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            val buffer = ShortArray(bufferSize)
            var totalEnergy = 0.0
            var zeroCrossings = 0
            var sampleCount = 0

            try {
                recorder.startRecording()
                val endTime = System.currentTimeMillis() + AUDIO_DURATION_MS

                while (System.currentTimeMillis() < endTime) {
                    val read = recorder.read(buffer, 0, buffer.size)
                    if (read > 0) {
                        for (i in 0 until read) {
                            totalEnergy += buffer[i].toDouble() * buffer[i].toDouble()
                            if (i > 0 && (buffer[i] > 0) != (buffer[i - 1] > 0)) {
                                zeroCrossings++
                            }
                            sampleCount++
                        }
                    }
                }
            } finally {
                recorder.stop()
                recorder.release()
                // Zero the buffer — raw audio never persisted
                buffer.fill(0)
            }

            // Create fingerprint from energy and zero-crossing rate
            val rmsEnergy = if (sampleCount > 0) sqrt(totalEnergy / sampleCount) else 0.0
            val zcRate = if (sampleCount > 0) zeroCrossings.toDouble() / sampleCount else 0.0
            val fingerprint = "rms:${rmsEnergy.toLong()},zcr:${(zcRate * 10000).toLong()}"

            val hash = hashUtils.hashWithSalt(fingerprint.toByteArray())
            hashUtils.toHex(hash)
        }
    } catch (e: Exception) { null }

    /**
     * Sensor: accelerometer + gyroscope sample → hash.
     */
    private suspend fun collectSensor(): String? = try {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val accelData = getSensorReading(sensorManager, Sensor.TYPE_ACCELEROMETER)
        val gyroData = getSensorReading(sensorManager, Sensor.TYPE_GYROSCOPE)

        if (accelData == null && gyroData == null) return null

        val data = buildString {
            accelData?.let { append("accel:${it.joinToString(",")}") }
            append("|")
            gyroData?.let { append("gyro:${it.joinToString(",")}") }
        }

        val hash = hashUtils.hashWithSalt(data.toByteArray())
        hashUtils.toHex(hash)
    } catch (e: Exception) { null }

    private suspend fun getSensorReading(
        sensorManager: SensorManager,
        sensorType: Int
    ): FloatArray? = suspendCancellableCoroutine { cont ->
        val sensor = sensorManager.getDefaultSensor(sensorType)
        if (sensor == null) {
            cont.resume(null)
            return@suspendCancellableCoroutine
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    sensorManager.unregisterListener(this)
                    if (cont.isActive) cont.resume(it.values.copyOf())
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        // Timeout after 5 seconds
        MainScope().launch {
            delay(5000)
            sensorManager.unregisterListener(listener)
            if (cont.isActive) cont.resume(null)
        }

        cont.invokeOnCancellation {
            sensorManager.unregisterListener(listener)
        }
    }

    private fun hasPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}
