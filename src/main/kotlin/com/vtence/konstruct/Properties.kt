package com.vtence.konstruct


sealed class Property<in T, V> {
    abstract infix fun of(value: Provider<V>): PropertyProvider<T>

    infix fun of(value: V): PropertyProvider<T> = of(SameValue(value))
}


internal class SingleProperty<T, V>: Property<T, V>() {
    override fun of(value: Provider<V>): PropertyProvider<T> {
        return PropertyProvider { collector ->
            collector.with(this, value)
        }
    }
}

fun <T, V> property(): Property<T, V> = SingleProperty()


internal class PropertyComposition<T, V>(private val collect: PropertyCollector<T>.(Provider<V>) -> Unit): Property<T, V>() {
    override fun of(value: Provider<V>): PropertyProvider<T> {
        return PropertyProvider { collector ->
            collect(collector, theSame(value))
        }
    }
}

fun <T, V> compose(collect: PropertyCollector<T>.(Provider<V>) -> Unit): Property<T, V> = PropertyComposition(collect)
