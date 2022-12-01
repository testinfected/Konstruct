package com.vtence.konstruct

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import com.natpryce.hamkrest.sameInstance
import kotlin.test.Test


class KonstructionTests {
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

    val small = compose<Thing, String> { with(name, it).with(size, Size.S) }

    val thing: Factory<Thing> = Factory {
        Thing(
            name = it[name] ?: "no name",
            size = it[size] ?: Size.M,
            description = it.get(description, "no description")
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

    class Pocket(
        val content: Thing?,
    )

    val content = property<Pocket, Thing?>()
    val pocket = Factory {
        Pocket(it[content])
    }

    @Test
    fun `can use factories as property values`() {
        val bag = make(a(pocket, with(content, a(thing, with(name, "ruler")))))

        assertThat(bag.content?.name, present(equalTo("ruler")))
    }

    @Test
    fun `creates new property values anew from factory for every made thing`() {
        val pocket = a(pocket, with(content, a(thing, with(name, "tool"))))

        val left = make(pocket)
        val right = make(pocket)

        assertThat(right.content, !sameInstance(left.content))
    }

    @Test
    fun `can also reuse the same property value instance for every made thing`() {
        val pocket = a(pocket, with(content, theSame(thing, with(name, "tool"))))

        val bag1 = make(pocket)
        val bag2 = make(pocket)

        assertThat(bag2.content, sameInstance(bag1.content))
    }

    @Test
    fun `computes same property value instance on first access only`() {
        var count = 0
        val counting = theSame(Provider { count++ } )

        assertThat(count, equalTo(0))
        make(counting)
        assertThat(count, equalTo(1))
        make(counting)
        assertThat(count, equalTo(1))
    }

    class Backpack(
        val things: Iterable<Thing>,
    )

    val things = property<Backpack, Iterable<Thing>>()
    val backpack = Factory {
        Backpack(it.get(things) ?: emptyList())
    }

    @Test
    fun `creates collection elements anew for every made thing`() {
        val bag = a(
            backpack, with(
                things,
                aListOf(
                    some(thing, with(name, "socks")),
                    a(thing, with(name, "jacket"))
                )
            )
        )

        val duffelBag = bag()
        val sportBag = bag()

        assertThat("different things", duffelBag.things, !sameInstance(sportBag.things))
        assertThat("different socks", duffelBag.things.first(), !sameInstance(sportBag.things.first()))
        assertThat("different jacket", duffelBag.things.last(), !sameInstance(sportBag.things.last()))
    }

    @Test
    fun `can reuse the same collection elements for every made thing`() {
        val bag = a(
            backpack, with(
                things,
                aSetOf(
                    theSame(thing, with(name, "socks")),
                    theSame(thing, with(name, "jacket"))
                )
            )
        )

        val duffelBag = bag()
        val sportBag = bag()

        assertThat("different things", duffelBag.things, !sameInstance(sportBag.things))
        assertThat("same socks", duffelBag.things.first(), sameInstance(sportBag.things.first()))
        assertThat("same jacket", duffelBag.things.last(), sameInstance(sportBag.things.last()))
    }

    @Test
    fun `can reuse the same collection for every made thing`() {
        val bag = a(
            backpack, with(
                things,
                theSameListOf(
                    some(thing, with(name, "socks")),
                    a(thing, with(name, "jacket"))
                )
            )
        )

        val duffelBag = bag()
        val sportBag = bag()

        assertThat("same things", duffelBag.things, sameInstance(sportBag.things))
        assertThat("same socks", duffelBag.things.first(), sameInstance(sportBag.things.first()))
        assertThat("same jacket", duffelBag.things.last(), sameInstance(sportBag.things.last()))
    }

    @Test
    fun `can share partial definitions between makers`() {
        val screw = a(thing, with(name, "screw"), with(size, Size.M))

        fun makeScrew(ofSize: Size) = make(screw.but(with(size, ofSize)))

        val mediumScrew = makeScrew(Size.M)
        val smallScrew = makeScrew(Size.S)

        assertThat("medium screw name", mediumScrew.name, equalTo("screw"))
        assertThat("small screw name", smallScrew.name, equalTo("screw"))

        assertThat("medium screw size", mediumScrew.size, equalTo(Size.M))
        assertThat("small screw size", smallScrew.size, equalTo(Size.S))
    }
}