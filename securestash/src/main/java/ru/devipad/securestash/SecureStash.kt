package ru.devipad.securestash

import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class SecureStash<T>(
    private val secureStash: SecureSharedPreferences,
    private val key: String,
    private val default: T
) : ReadWriteProperty<Any?, T> {

    @Throws(IllegalArgumentException::class, SecurityException::class)
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        when (value) {
            is Date -> secureStash[key] = value.time.toString()
            is String -> secureStash[key] = value
            is Int,
            is Long,
            is Float,
            is Double,
            is Boolean,
            is BigDecimal,
            is BigInteger -> secureStash[key] = value.toString()
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }
    }

    @Throws(IllegalArgumentException::class, SecurityException::class)
    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val result: Any? = when (default) {
            is Int -> secureStash[key]?.toInt()
            is Date -> getDate(secureStash[key])
            is Long -> secureStash[key]?.toLong()
            is Float -> secureStash[key]?.toFloat()
            is Double -> secureStash[key]?.toDouble()
            is String -> secureStash[key]
            is Boolean -> secureStash[key]?.toBoolean()
            is BigDecimal -> secureStash[key]?.toBigDecimal()
            is BigInteger -> secureStash[key]?.toBigInteger()
            else -> throw IllegalArgumentException("This type can't be load from Preferences")
        }
        return result as T? ?: default
    }

    private fun getDate(value: String?): Date {
        value ?: return default as Date
        return Date(value.toLong())
    }
}
