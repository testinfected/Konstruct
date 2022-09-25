package com.vtence.flintstone


fun <T> make(maker: Provider<T>): T = maker.invoke()

fun <T> make(vararg makers: Provider<T>): List<T> = makers.map { make(it) }


fun <T, V> with(property: Property<T, V>, value: V): PropertyProvider<T> = property.of(value)

fun <T, V> with(property: Property<T, V>, value: Provider<V>): PropertyProvider<T> = property.of(value)

fun <T, V> withNull(property: Property<T, V?>): PropertyProvider<T> = with(property, null)


fun <T, V> having(property: Property<T, V>, value: V): PropertyProvider<T> = with(property, value)

fun <T, V> having(property: Property<T, V>, value: Provider<V>): PropertyProvider<T> = with(property, value)

fun <T, V> havingNull(property: Property<T, V?>): PropertyProvider<T> = withNull(property)


fun <T> theSame(thing: Factory<T>, vararg properties: PropertyProvider<T>): Provider<T> = theSame(Maker.a(thing, *properties))

fun <T> theSame(value: Provider<T>): Provider<T> = SameValueProvider(value())
