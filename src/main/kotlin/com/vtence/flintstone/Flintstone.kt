package com.vtence.flintstone


object Flintstone {
    fun <T> make(maker: Provider<T>): T = maker.invoke()

    fun <T> make(vararg makers: Provider<T>): List<T> = makers.map { make(it) }


    fun <T, V> with(property: Property<T, V>, value: V): PropertyProvider<T> = property.of(value)

    fun <T, V> with(property: Property<T, V>, value: Provider<V>): PropertyProvider<T> = property.of(value)

    fun <T, V> withNull(property: Property<T, V?>): PropertyProvider<T> = with(property, null)


    fun <T, V> having(property: Property<T, V>, value: V): PropertyProvider<T> = with(property, value)

    fun <T, V> having(property: Property<T, V>, value: Provider<V>): PropertyProvider<T> = with(property, value)

    fun <T, V> havingNull(property: Property<T, V?>): PropertyProvider<T> = withNull(property)


    fun <T> theSame(thing: Factory<T>, vararg properties: PropertyProvider<T>): Provider<T> = theSame(Maker.a(thing, *properties))

    fun <T> theSame(value: Provider<T>): Provider<T> = theSame(value())

    fun <T> theSame(value: T): Provider<T> = SameValue(value)


    fun <T> aListOf(vararg values: Provider<T>): Provider<List<T>> = ListOfValues(values.toList())

    fun <T> aSetOf(vararg values: Provider<T>): Provider<Set<T>> = SetOfValues(values.toSet())

    fun <T : Comparable<T>> aSortedSetOf(vararg values: Provider<T>): Provider<Set<T>> = SortedSetOfValues(values.toSet())

    fun <T> theSameListOf(vararg values: Provider<T>): Provider<List<T>> = theSame(aListOf(*values))

    fun <T> theSameSetOf(vararg values: Provider<T>): Provider<Set<T>> = theSame(aSetOf(*values))

    fun <T : Comparable<T>> theSameSortedSetOf(vararg values: Provider<T>): Provider<Set<T>> = theSame(aSortedSetOf(*values))
}
