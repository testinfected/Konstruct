package com.vtence.konstruct


class Maker<T>(
    private val factory: Factory<T>,
    private val properties: Map<Property<T, *>, Provider<*>>,
) : Provider<T>, PropertyLookup<T>, PropertyCollector<T> {

    override fun invoke(): T {
        return factory.build(this)
    }

    override fun <V> get(property: Property<T, V>): V? {
        return if (property in properties) getValue(property) else null
    }

    override fun <V> get(property: Property<T, V>, defaultValue: V): V {
        return if (property in properties) getValue(property) else defaultValue
    }

    @Suppress("UNCHECKED_CAST")
    private fun <V> getValue(property: Property<T, V>) = properties.getValue(property).invoke() as V

    override fun <V> with(property: Property<T, V>, value: Provider<V>): Maker<T> = with(property of value)

    fun with(property: PropertyProvider<T>): Maker<T> = but(property)

    override fun <V> with(property: Property<T, V>, value: V): Maker<T> = with(property, the(value))

    override fun <V> withNull(property: Property<T, V?>): Maker<T> = with(property, NullValue)

    fun but(vararg properties: PropertyProvider<T>): Maker<T> {
        val bag = PropertyBag(this.properties)
        bag.collect(*properties)
        return Maker(factory, bag.properties)
    }
}


fun <T> a(thing: Factory<T>, vararg properties: PropertyProvider<T>): Maker<T> {
    val bag = PropertyBag<T>()
    bag.collect(*properties)
    return Maker(thing, bag.properties)
}

fun <T> an(thing: Factory<T>, vararg properties: PropertyProvider<T>): Maker<T> {
    return a(thing, *properties)
}

fun <T> some(thing: Factory<T>, vararg properties: PropertyProvider<T>): Maker<T> {
    return a(thing, *properties)
}


private class PropertyBag<T>(
    properties: Map<Property<T, *>, Provider<*>> = emptyMap()
): PropertyCollector<T> {
    val properties = properties.toMutableMap()

    override fun <V> with(property: Property<T, V>, value: Provider<V>): PropertyCollector<T> {
        return apply {
            when(property) {
                is SingleProperty -> properties[property] = value
                is PropertyComposition -> with(property of value)
            }
        }
    }

    fun with(property: PropertyProvider<T>): PropertyCollector<T> = apply {
        property.provideTo(this)
    }

    fun collect(vararg properties: PropertyProvider<T>) {
        properties.forEach { it.provideTo(this) }
    }
}