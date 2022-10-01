package com.vtence.konstruct

fun interface PropertyProvider<in T> {
    fun provideTo(collector: PropertyCollector<T>)
}
