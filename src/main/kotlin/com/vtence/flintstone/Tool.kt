package com.vtence.flintstone


class Tool<T>(
    private val instantiator: Instantiator<T>,
) : Provider<T>, PropertyLookup<T>, PropertyCollector<T> {

    private val properties: MutableMap<Property<T, *>, Any?> = mutableMapOf()

    override fun value(): T {
        return instantiator.instantiate(this)
    }

    override fun <V> valueOf(property: Property<T, V>, defaultValue: V): V {
        @Suppress("UNCHECKED_CAST")
        return if (property in properties)
            properties.getValue(property) as V
        else
            defaultValue

    }

    companion object {
        fun <T> a(instantiator: Instantiator<T>, vararg properties: PropertyProvider<T>): Tool<T> {
            return Tool(instantiator).also { tool ->
                properties.forEach { it.provideTo(tool) }
            }
        }

        fun <T> an(instantiator: Instantiator<T>, vararg properties: PropertyProvider<T>): Tool<T> {
            return a(instantiator, *properties)
        }

        fun <T> some(instantiator: Instantiator<T>, vararg properties: PropertyProvider<T>): Tool<T> {
            return a(instantiator, *properties)
        }
    }

    override fun <V> with(property: Property<T, V>, value: V): Tool<T> = apply {
        properties[property] = value
    }

    fun with(provider: PropertyProvider<T>): Tool<T> = apply {
        provider.provideTo(this)
    }
}