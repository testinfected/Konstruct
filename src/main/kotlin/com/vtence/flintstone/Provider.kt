package com.vtence.flintstone

import java.util.*


fun interface Provider<out T>: () -> T


class SameValue<T>(private val value: T) : Provider<T> {
    override fun invoke(): T {
        return value
    }
}


class ListOfValues<T>(private val values: List<Provider<T>>) : Provider<List<T>> {
    override fun invoke(): List<T> {
        return values.map { it() }
    }
}

class SetOfValues<T>(private val values: Set<Provider<T>>) : Provider<Set<T>> {
    override fun invoke(): Set<T> {
        return values.map { it() }.toSet()
    }
}

class SortedSetOfValues<T : Comparable<T>>(private val values: Set<Provider<T>>) : Provider<SortedSet<T>> {
    override fun invoke(): SortedSet<T> {
        return values.map { it() }.toSortedSet(Comparator.naturalOrder())
    }
}
