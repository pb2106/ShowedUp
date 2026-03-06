package com.showedup.app.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.showedup.app.data.entity.SecurityEventType
import com.showedup.app.data.repository.SecurityRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages biometric authentication for app access.
 * 5-failure lockout (30 min), logged to SecurityEventLog.
 */
@Singleton
class BiometricAuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val securityRepository: SecurityRepository
) {

    companion object {
        private const val MAX_FAILURES = 5
        private const val LOCKOUT_DURATION_MS = 30 * 60 * 1000L // 30 minutes
    }

    fun canAuthenticate(): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun authenticate(
        activity: FragmentActivity,
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onLockout: () -> Unit
    ) {
        val scope = CoroutineScope(Dispatchers.IO)

        // Check lockout
        scope.launch {
            val failures = securityRepository.countRecentBiometricFailures(LOCKOUT_DURATION_MS)
            if (failures >= MAX_FAILURES) {
                CoroutineScope(Dispatchers.Main).launch {
                    onLockout()
                }
                return@launch
            }

            CoroutineScope(Dispatchers.Main).launch {
                showBiometricPrompt(activity, title, subtitle, onSuccess, onError, scope)
            }
        }
    }

    private fun showBiometricPrompt(
        activity: FragmentActivity,
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        scope: CoroutineScope
    ) {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                scope.launch {
                    securityRepository.logEvent(SecurityEventType.BIOMETRIC_SUCCESS)
                }
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                scope.launch {
                    securityRepository.logEvent(
                        SecurityEventType.BIOMETRIC_FAILURE,
                        "errorCode=$errorCode"
                    )
                }
                onError(errString.toString())
            }

            override fun onAuthenticationFailed() {
                scope.launch {
                    securityRepository.logEvent(SecurityEventType.BIOMETRIC_FAILURE)
                }
            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        BiometricPrompt(activity, executor, callback).authenticate(promptInfo)
    }
}
