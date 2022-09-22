package com.vtence.flintstone


abstract class Property<in T, V> {
    abstract infix fun of(value: V): PropertyProvider<T>

    companion object {
        fun <T, V> property(): Property<T, V> = object: Property<T, V>() {
            override fun of(value: V): PropertyProvider<T> {
                return PropertyProvider {
                    it.with(this, value)
                }
            }
        }

        fun <T, V> property(composite: PropertyCollector<T>.(V) -> Unit): Property<T, V> = object : Property<T, V>() {
            override fun of(value: V): PropertyProvider<T> {
                return PropertyProvider { collector ->
                    composite(collector, value)
                }
            }
        }
    }
}