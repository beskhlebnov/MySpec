package com.example.myspec.keystore

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object KeystoreManager {
    private const val KEYSTORE_ALIAS = "refresh_token_key_v2"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    private const val TRANSFORMATION = "AES/GCM/NoPadding"


    private fun createKeyIfNotExists(){
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }
        if (!keyStore.containsAlias(KEYSTORE_ALIAS)){
            val keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM, ANDROID_KEYSTORE)
            val parameterSpec = KeyGenParameterSpec.Builder(
                KEYSTORE_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()
            keyGenerator.init(parameterSpec)
            keyGenerator.generateKey()
        }
    }

    private fun getKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }
        return keyStore.getKey(KEYSTORE_ALIAS, null) as SecretKey
    }

    fun encrypt(data: String): Pair<ByteArray, ByteArray> {
        createKeyIfNotExists()

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(data.toByteArray())
        return iv to encryptedData
    }

    fun decrypt(iv: ByteArray, encryptedData: ByteArray): String{
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)
        val decryptedData = cipher.doFinal(encryptedData)
        return String(decryptedData)
    }

}