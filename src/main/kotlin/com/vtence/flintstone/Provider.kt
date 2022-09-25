package com.vtence.flintstone


fun interface Provider<out T>: () -> T


class SameValueProvider<T>(private val value: T) : Provider<T> {
    override fun invoke(): T {
        return value
    }
}


fun <T> theSame(thing: Factory<T>, vararg properties: PropertyProvider<T>): Provider<T> {
    return theSame(Maker.a(thing, *properties))
}

fun <T> theSame(value: Provider<T>): Provider<T> {
    return SameValueProvider(value())
}