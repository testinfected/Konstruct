package com.vtence.konstruct


abstract class Property<in T, V> {
    abstract infix fun of(value: Provider<V>): PropertyProvider<T>

    infix fun of(value: V): PropertyProvider<T> = of(SameValue(value))
}


fun <T, V> property(): Property<T, V> = object : Property<T, V>() {
    override fun of(value: Provider<V>): PropertyProvider<T> {
        return PropertyProvider { collector ->
            collector.with(this, value)
        }
    }
}

fun <T, V> define(definition: PropertyCollector<T>.(V) -> Unit): Property<T, V> = object : Property<T, V>() {
    override fun of(value: Provider<V>): PropertyProvider<T> {
        return PropertyProvider { collector ->
            definition(collector, value())
        }
    }
}
