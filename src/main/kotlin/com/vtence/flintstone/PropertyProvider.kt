package com.vtence.flintstone

fun interface PropertyProvider<T> {
    fun provideTo(collector: PropertyCollector<T>)
}