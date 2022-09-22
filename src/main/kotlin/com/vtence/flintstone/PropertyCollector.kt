package com.vtence.flintstone

interface PropertyCollector<T> {
    fun <V> with(property: Property<T, V>, value: V): PropertyCollector<T>
}

