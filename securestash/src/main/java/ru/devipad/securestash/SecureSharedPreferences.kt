package ru.devipad.securestash

import android.content.SharedPreferences

class SecureSharedPreferences(
    private val sharedPreferences: SharedPreferences,
    key: String
) {
    private val crypto = Crypto.getInstance(key)

    @Throws(IllegalArgumentException::class, EncryptException::class)
    operator fun set(key: String, value: String) {
        require(key.isNotBlank()) { "Key should not be empty" }

        val encodedString = crypto.encrypt(value)

        sharedPreferences.edit().putString(key, encodedString).apply()
    }

    @Throws(IllegalArgumentException::class, EncryptException::class)
    operator fun get(key: String): String? {
        require(key.isNotBlank()) { "Key should not be empty" }

        val encodedString = sharedPreferences.getString(key, null) ?: return null

        return crypto.decrypt(encodedString)
    }

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}