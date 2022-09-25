package com.vtence.flintstone


fun interface Provider<out T>: () -> T


class SameValueProvider<T>(private val value: T) : Provider<T> {
    override fun invoke(): T {
        return value
    }
}


class ListOfValuesProvider<T>(private val values: List<Provider<T>>) : Provider<List<T>> {
    override fun invoke(): List<T> {
        return values.map { it() }
    }
}
