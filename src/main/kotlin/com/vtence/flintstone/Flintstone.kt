package com.vtence.flintstone


object Flintstone {
    fun <T> make(tool: Tool<T>): T {
        return tool.value()
    }

    fun <T, V> with(property: Property<T, V>, value: V): PropertyProvider<T> {
        return property.of(value)
    }
}
