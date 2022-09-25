package com.vtence.flintstone


fun interface Factory<T> {
    fun create(properties: PropertyLookup<T>): T
}


class Maker<T>(
    private val factory: Factory<T>,
    properties: Map<Property<T, *>, Provider<*>> = emptyMap(),
) : Provider<T>, PropertyLookup<T>, PropertyCollector<T> {

    private val properties: MutableMap<Property<T, *>, Provider<*>> = properties.toMutableMap()

    override fun invoke(): T {
        return factory.create(this)
    }

    override fun <V> valueOf(property: Property<T, V>, defaultValue: V): V {
        @Suppress("UNCHECKED_CAST")
        return if (property in properties)
            properties.getValue(property).invoke() as V
        else
            defaultValue

    }

    override fun <V> with(property: Property<T, V>, value: Provider<V>): Maker<T> = apply {
        properties[property] = value
    }

    override fun <V> with(property: Property<T, V>, value: V): Maker<T> = with(property, SameValue(value))

    fun with(property: PropertyProvider<T>): Maker<T> = apply {
        property.provideTo(this)
    }

    fun but(vararg properties: PropertyProvider<T>): Maker<T> = but().apply {
        properties.forEach { it.provideTo(this) }
    }

    fun but(): Maker<T> = Maker(factory, properties)

    companion object {
        fun <T> a(thing: Factory<T>, vararg properties: PropertyProvider<T>): Maker<T> {
            return Maker(thing).also { maker ->
                properties.forEach { it.provideTo(maker) }
            }
        }

        fun <T> an(thing: Factory<T>, vararg properties: PropertyProvider<T>): Maker<T> {
            return a(thing, *properties)
        }

        fun <T> some(thing: Factory<T>, vararg properties: PropertyProvider<T>): Maker<T> {
            return a(thing, *properties)
        }
    }
}
