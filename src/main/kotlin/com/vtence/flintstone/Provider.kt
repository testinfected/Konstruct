package com.vtence.flintstone


fun interface Provider<out T> {
    fun value(): T
}