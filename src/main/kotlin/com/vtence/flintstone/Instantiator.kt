package com.vtence.flintstone


fun interface Instantiator<T> {
    fun instantiate(properties: PropertyLookup<T>): T
}