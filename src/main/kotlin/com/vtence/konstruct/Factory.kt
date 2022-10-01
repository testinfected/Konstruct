package com.vtence.konstruct


fun interface Factory<T> {
    fun build(properties: PropertyLookup<T>): T
}
