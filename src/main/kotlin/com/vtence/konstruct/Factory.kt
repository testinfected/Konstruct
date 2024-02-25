package com.vtence.konstruct


fun interface Factory<T> {
    fun build(properties: PropertyLookup<T>): T
}

interface Fabricator<T> {
    val factory: Factory<T>
}
