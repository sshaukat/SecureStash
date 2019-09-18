package ru.devipad.securestash

open class EncryptException(message: String?, exception: Throwable? = null)
    : RuntimeException(message, exception)
