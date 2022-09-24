package com.vtence.flintstone


object Flintstone {
    fun <T> make(maker: Provider<T>): T {
        return maker.invoke()
    }

    fun <T> make(vararg makers: Provider<T>): List<T> {
        return makers.map { make(it) }
    }

    fun <T, V> with(property: Property<T, V>, value: V): PropertyProvider<T> {
        return property.of(value)
    }

    fun <T, V> with(property: Property<T, V>, value: Provider<V>): PropertyProvider<T> {
        return property.of(value)
    }

    fun <T, V> withNull(property: Property<T, V?>): PropertyProvider<T> {
        return with(property, null)
    }
}
