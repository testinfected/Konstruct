package com.vtence.konstruct



fun <T> make(thing: Provider<T>): T = thing()

fun <T> make(vararg things: Provider<T>): List<T> = things.map { make(it) }


fun <T, V> with(property: Property<T, V>, value: V): PropertyProvider<T> = property.of(value)

fun <T, V> with(property: Property<T, V>, value: Provider<V>): PropertyProvider<T> = property.of(value)

fun <T, V> withNull(property: Property<T, V?>): PropertyProvider<T> = with(property, NullValue)


fun <T, V> having(property: Property<T, V>, value: V): PropertyProvider<T> = with(property, value)

fun <T, V> having(property: Property<T, V>, valueProvider: Provider<V>): PropertyProvider<T> = with(property, valueProvider)

fun <T, V> havingNull(property: Property<T, V?>): PropertyProvider<T> = withNull(property)


fun <T> aListOf(vararg values: Provider<T>): Provider<List<T>> = ListOfValues(values.toList())

fun <T> aSetOf(vararg values: Provider<T>): Provider<Set<T>> = SetOfValues(values.toSet())

fun <T : Comparable<T>> aSortedSetOf(vararg values: Provider<T>): Provider<Set<T>> = SortedSetOfValues(values.toSet())


fun <T> theSame(value: Provider<T>): Provider<T> = theSame(value())

fun <T> theSame(value: T): Provider<T> = SameValue(value)

fun <T> theSame(thing: Factory<T>, vararg properties: PropertyProvider<T>): Provider<T> = theSame(a(thing, *properties))

fun <T> theSameListOf(vararg values: Provider<T>): Provider<List<T>> = theSame(aListOf(*values))

fun <T> theSameSetOf(vararg values: Provider<T>): Provider<Set<T>> = theSame(aSetOf(*values))

fun <T : Comparable<T>> theSameSortedSetOf(vararg values: Provider<T>): Provider<Set<T>> = theSame(aSortedSetOf(*values))
