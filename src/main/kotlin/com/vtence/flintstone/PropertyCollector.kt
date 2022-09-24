package com.vtence.flintstone

interface PropertyCollector<out T> {

    fun <V> with(property: Property<T, V>, value: Provider<V>): PropertyCollector<T>

    fun <V> with(property: Property<T, V>, value: V): PropertyCollector<T>
}

