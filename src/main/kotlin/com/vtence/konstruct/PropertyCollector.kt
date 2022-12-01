package com.vtence.konstruct

interface PropertyCollector<out T> {
    fun <V> with(property: Property<T, V>, value: V): PropertyCollector<T>

    fun <V> with(property: Property<T, V>, value: Provider<V>): PropertyCollector<T>

    fun <V> withNull(property: Property<T, V?>): PropertyCollector<T>
}

fun <V, T> PropertyCollector<T>.having(property: Property<T, V>, value: V): PropertyCollector<T> = with(property, value)

fun <V, T> PropertyCollector<T>.having(property: Property<T, V>, value: Provider<V>): PropertyCollector<T> = with(property, value)

fun <V, T> PropertyCollector<T>.havingNull(property: Property<T, V?>): PropertyCollector<T> = withNull(property)
