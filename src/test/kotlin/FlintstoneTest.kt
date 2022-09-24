package com.vtence.flintstone

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import com.natpryce.hamkrest.sameInstance
import com.vtence.flintstone.Flintstone.make
import com.vtence.flintstone.Flintstone.with
import com.vtence.flintstone.Flintstone.withNull
import com.vtence.flintstone.Maker.Companion.a
import com.vtence.flintstone.Property.Companion.property
import kotlin.test.Test


class FlintstoneTest {
    enum class Size {
        S, M, L
    }

    class Thing(
        val name: String,
        val size: Size,
        val description: String?,
    )

    val name = property<Thing, String>()
    val size = property<Thing, Size>()
    val description = property<Thing, String?>()
    val small = property<Thing, String> { with(name, it).with(size, Size.S) }

    val thing: Factory<Thing> = Factory {
        Thing(
            name = it.valueOf(name, "no name"),
            size = it.valueOf(size, Size.M),
            description = it.valueOf(description, "no description")
        )
    }

    @Test
    fun `makes things using default values`() {
        val crane = make(a(thing))

        assertThat("default name", crane.name, equalTo("no name"))
        assertThat("default size", crane.size, equalTo(Size.M))
        assertThat("default description", crane.description, present(equalTo("no description")))
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
        val hammer = make(a(thing, with(small, "hammer")))

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

    @Test
    fun `can make things with null properties`() {
        val thing = make(a(thing, withNull(description)))

        assertThat("null value", thing.description, absent());
    }

    class Holder(
        val content: Thing?
    )

    val content = property<Holder, Thing?>()
    val holder = Factory {
        Holder(it.valueOf(content, null))
    }

    @Test
    fun `can use factories as property values`() {
        val bag = make(a(holder, with(content, a(thing, with(name, "ruler")))))

        assertThat(bag.content?.name, present(equalTo("ruler")))
    }

    @Test
    fun `makes new property from factory for every made thing`() {
        val bag = a(holder, with(content, a(thing, with(name, "..."))))

        val bag1 = make(bag)
        val bag2 = make(bag)

        assertThat(bag2.content, !sameInstance(bag1.content))
    }
}