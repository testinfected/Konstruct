package com.vtence.flintstone

import com.natpryce.hamkrest.assertion.*
import com.natpryce.hamkrest.equalTo
import com.vtence.flintstone.Flintstone.with
import com.vtence.flintstone.Flintstone.make
import com.vtence.flintstone.Property.Companion.property
import com.vtence.flintstone.Tool.Companion.a
import kotlin.test.Test


enum class Size {
    S, M, L
}

class Thing(
    val name: String,
    val size: Size
)


val name = property<Thing, String>()
val size = property<Thing, Size>()

val small = property<Thing, String> {
    with(name, it).with(size, Size.S)
}

val thing: Instantiator<Thing> = Instantiator {
    Thing(
        name = it.valueOf(name, "crane"),
        size = it.valueOf(size, Size.M)
    )
}


class FlintstoneTest {

    @Test
    fun `making things using default values`() {
        val crane = make(a(thing))

        assertThat("default name", crane.name, equalTo("crane"))
        assertThat("default size", crane.size, equalTo(Size.M))
    }

    @Test
    fun `overriding default values with explicit values`() {
        val hammer = make(a(thing).with(name, "hammer"))

        assertThat("explicit name", hammer.name, equalTo("hammer"))
    }

    @Test
    fun `overriding values that are already set`() {
        val hammer = make(a(thing).with(name of "hammer").with(name of "shovel"))

        assertThat("overridden name", hammer.name, equalTo("shovel"))
    }

    @Test
    fun `using composite properties to act on a group of properties`() {
        val hammer = make(a(thing,
           with(small, "hammer"),
        ))

        assertThat("name", hammer.name, equalTo("hammer"))
        assertThat("size", hammer.size, equalTo(Size.S))
    }
}