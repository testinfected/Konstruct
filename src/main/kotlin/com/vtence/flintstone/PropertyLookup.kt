package com.vtence.flintstone

interface PropertyLookup<T> {
    fun <V> valueOf(property: Property<T, V>): V?

    fun <V> valueOf(property: Property<T, V>, defaultValue: V): V
}