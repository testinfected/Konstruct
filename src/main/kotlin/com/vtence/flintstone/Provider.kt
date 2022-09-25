package com.vtence.flintstone


fun interface Provider<out T>: () -> T


class SameValueProvider<T>(private val value: T) : Provider<T> {
    override fun invoke(): T {
        return value
    }
}
