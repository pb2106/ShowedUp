package com.showedup.app.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.*
import java.security.spec.ECGenParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages ECDSA P-256 key pair in Android Keystore.
 * Private key never leaves the secure hardware — sign and verify only.
 */
@Singleton
class KeystoreManager @Inject constructor() {

    companion object {
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val KEY_ALIAS = "showedup_ecdsa_key"
        private const val SIGNATURE_ALGORITHM = "SHA256withECDSA"
    }

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(KEYSTORE_PROVIDER).apply { load(null) }
    }

    /**
     * Generates an ECDSA P-256 key pair if one doesn't exist.
     * Key requires user authentication and unlocked device.
     */
    fun generateKeyPairIfNeeded() {
        if (keyStore.containsAlias(KEY_ALIAS)) return

        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        )
            .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setUserAuthenticationRequired(true)
            .setUserAuthenticationParameters(
                0, // require auth for every operation
                KeyProperties.AUTH_BIOMETRIC_STRONG or KeyProperties.AUTH_DEVICE_CREDENTIAL
            )
            .setUnlockedDeviceRequired(true)
            .setIsStrongBoxBacked(false) // fallback gracefully
            .build()

        val kpg = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            KEYSTORE_PROVIDER
        )
        kpg.initialize(spec)
        kpg.generateKeyPair()
    }

    /**
     * Signs data with the ECDSA private key.
     * @return DER-encoded ECDSA signature
     */
    fun sign(data: ByteArray): ByteArray {
        val privateKey = keyStore.getKey(KEY_ALIAS, null) as PrivateKey
        return Signature.getInstance(SIGNATURE_ALGORITHM).run {
            initSign(privateKey)
            update(data)
            sign()
        }
    }

    /**
     * Verifies an ECDSA signature against the stored public key.
     */
    fun verify(data: ByteArray, signature: ByteArray): Boolean {
        val certificate = keyStore.getCertificate(KEY_ALIAS)
        return Signature.getInstance(SIGNATURE_ALGORITHM).run {
            initVerify(certificate)
            update(data)
            verify(signature)
        }
    }

    /**
     * Returns the public key bytes for logging/display (not the private key).
     */
    fun getPublicKeyBytes(): ByteArray {
        val certificate = keyStore.getCertificate(KEY_ALIAS)
        return certificate.publicKey.encoded
    }

    /**
     * Checks if the key pair already exists.
     */
    fun hasKeyPair(): Boolean = keyStore.containsAlias(KEY_ALIAS)
}
