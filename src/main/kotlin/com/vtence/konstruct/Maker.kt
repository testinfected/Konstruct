package com.vtence.konstruct


class Maker<T>(
    private val factory: Factory<T>,
    private val properties: PropertyBag<T>
) : Provider<T>, PropertyLookup<T> by properties, PropertyCollector<T> {

    override fun invoke(): T {
        return factory.build(this)
    }

    override fun <V> with(property: Property<T, V>, value: Provider<V>): Maker<T> = with(property of value)

    fun with(property: PropertyProvider<T>): Maker<T> = but(property)

    override fun <V> with(property: Property<T, V>, value: V): Maker<T> = with(property, the(value))

    override fun <V> withNull(property: Property<T, V?>): Maker<T> = with(property, NullValue)

    fun but(vararg properties: PropertyProvider<T>): Maker<T> {
        val bag = PropertyBag(this.properties)
        bag.collect(*properties)
        return Maker(factory, bag)
    }
}

fun <T> a(thing: Fabricator<T>, vararg properties: PropertyProvider<T>): Maker<T> {
    return a(thing.factory, *properties)
}

fun <T, F: Fabricator<T>> a(thing: F, collect: PropertyCollector<T>.(F) -> Unit): Maker<T> {
    val props = PropertyBag<T>()
    collect(props, thing)
    return Maker(thing.factory, props)
}

fun <T> a(thing: Factory<T>, vararg properties: PropertyProvider<T>): Maker<T> {
    val props = PropertyBag<T>()
    props.collect(*properties)
    return Maker(thing, props)
}

fun <T> an(thing: Fabricator<T>, vararg properties: PropertyProvider<T>): Maker<T> {
    return a(thing, *properties)
}

fun <T, F: Fabricator<T>> an(thing: F, collect: PropertyCollector<T>.(F) -> Unit): Maker<T> {
    return a(thing, collect)
}

fun <T> an(thing: Factory<T>, vararg properties: PropertyProvider<T>): Maker<T> {
    return a(thing, *properties)
}

fun <T> some(thing: Fabricator<T>, vararg properties: PropertyProvider<T>): Maker<T> {
    return a(thing, *properties)
}

fun <T, F: Fabricator<T>> some(thing: F, collect: PropertyCollector<T>.(F) -> Unit): Maker<T> {
    return a(thing, collect)
}

fun <T> some(thing: Factory<T>, vararg properties: PropertyProvider<T>): Maker<T> {
    return a(thing, *properties)
}


class PropertyBag<T>(
    properties: Map<Property<T, *>, Provider<*>> = emptyMap()
): PropertyCollector<T>, PropertyLookup<T> {

    constructor(bag: PropertyBag<T>): this(bag.properties)

    private val properties = properties.toMutableMap()

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

    override fun <V> get(property: Property<T, V>): V? {
        return if (property in this) getValue(property) else null
    }

    override fun <V> get(property: Property<T, V>, defaultValue: V): V {
        return if (property in this) getValue(property) else defaultValue
    }

    @Suppress("UNCHECKED_CAST")
    private fun <V> getValue(property: Property<T, V>) = properties.getValue(property).invoke() as V

    override fun <V> contains(property: Property<T, V>): Boolean {
        return property in properties
    }
}