package com.vtence.flintstone

fun interface PropertyProvider<in T> {
    fun provideTo(collector: PropertyCollector<T>)
}
