package com.vtence.flintstone

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.vtence.flintstone.Flintstone.make
import com.vtence.flintstone.Flintstone.with
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
val small = property<Thing, String> { with(name, it).with(size, Size.S) }

val thing: Instantiator<Thing> = Instantiator {
    Thing(
        name = it.valueOf(name, "crane"),
        size = it.valueOf(size, Size.M)
    )
}


class FlintstoneTest {

    @Test
    fun `makes things using default values`() {
        val crane = make(a(thing))

        assertThat("default name", crane.name, equalTo("crane"))
        assertThat("default size", crane.size, equalTo(Size.M))
    }

    @Test
    fun `overrides default values with explicit values`() {
        val hammer = make(a(thing).with(name, "hammer"))

        assertThat("explicit name", hammer.name, equalTo("hammer"))
    }

    @Test
    fun `overrides values that are already set`() {
        val hammer = make(a(thing).with(name of "hammer").with(name of "shovel"))

        assertThat("overridden name", hammer.name, equalTo("shovel"))
    }

    @Test
    fun `acts on many properties at once`() {
        val hammer = make(a(thing,
           with(small, "hammer"),
        ))

        assertThat("name", hammer.name, equalTo("hammer"))
        assertThat("size", hammer.size, equalTo(Size.S))
    }

    @Test
    fun `makes many of the same thing at once`() {
        val tools = make(
            a(thing).with(name, "hammer"),
            a(thing).with(name, "wrench"),
        )

        val toolNames = tools.map { it.name }

        assertThat("tool names", toolNames, equalTo(listOf("hammer", "wrench")))
    }
}