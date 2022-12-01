package com.vtence.konstruct

interface PropertyLookup<T> {
    operator fun <V> get(property: Property<T, V>): V?

    fun <V> get(property: Property<T, V>, defaultValue: V): V

    fun <V> get(property: Property<T, V>, defaultValue: Provider<V>) = get(property, defaultValue())
}
