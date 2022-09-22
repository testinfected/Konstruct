package com.vtence.flintstone


object Flintstone {
    fun <T> make(tool: Provider<T>): T {
        return tool.value()
    }

    fun <T> make(vararg tools: Provider<T>): List<T> {
        return tools.map { make(it) }
    }

    fun <T, V> with(property: Property<T, V>, value: V): PropertyProvider<T> {
        return property.of(value)
    }
}
