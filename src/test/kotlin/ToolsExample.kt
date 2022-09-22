package com.vtence.flintstone

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.vtence.flintstone.AllenKeyMaker.allenKey
import com.vtence.flintstone.Flintstone.make
import com.vtence.flintstone.Flintstone.with
import com.vtence.flintstone.HouseholdToolMaker.color
import com.vtence.flintstone.HouseholdToolMaker.name
import com.vtence.flintstone.PliersMaker.pliers
import com.vtence.flintstone.Property.Companion.property
import com.vtence.flintstone.ScrewDriverMaker.flatHead
import com.vtence.flintstone.ScrewDriverMaker.screwDriver
import com.vtence.flintstone.TapeMaker.tape
import com.vtence.flintstone.Tool.Companion.a
import com.vtence.flintstone.Tool.Companion.an
import com.vtence.flintstone.Tool.Companion.some
import org.junit.jupiter.api.Test


enum class Color {
    BLUE, YELLOW, RED, BLACK
}

sealed class HouseholdTool(
    val name: String,
    val color: Color
)


object HouseholdToolMaker {
    val name = property<HouseholdTool, String>()
    val color = property<HouseholdTool, Color>()
}


class Tape(name: String, color: Color) : HouseholdTool(name, color)

object TapeMaker {
    val tape = Instantiator {
        Tape(
            name = it.valueOf(name, "standard tape"),
            color = it.valueOf(color, Color.YELLOW)
        )
    }
}

class AllenKey(name: String, color: Color) : HouseholdTool(name, color)

object AllenKeyMaker {
    val allenKey = Instantiator {
        AllenKey(
            name = it.valueOf(name, "standard hex key"),
            color = it.valueOf(color, Color.BLACK)
        )
    }
}

class Pliers(name: String, color: Color) : HouseholdTool(name, color)

object PliersMaker {
    val pliers = Instantiator {
        Pliers(
            name = it.valueOf(name, "standard pliers"),
            color = it.valueOf(color, Color.BLUE)
        )
    }
}


enum class Head {
    PHILIPS, FLAT
}

class ScrewDriver(name: String, color: Color, val head: Head): HouseholdTool(name, color)

object ScrewDriverMaker {
    val head = property<ScrewDriver, Head>()
    val flatHead = property<ScrewDriver, Color> { with(head, Head.FLAT).with(color, it) }

    val screwDriver = Instantiator {
        ScrewDriver(
            name = it.valueOf(name, "no name"),
            color = it.valueOf(color, Color.RED),
            head = it.valueOf(head, Head.FLAT)
        )
    }
}


class ToolsExample {
    @Test
    fun `how to make simple things using default values`() {
        val tape = make(a(tape))

        assertThat("name", tape.name, equalTo("standard tape"))
        assertThat("color", tape.color, equalTo(Color.YELLOW))
    }

    @Test
    fun `how to make simple things by specifying values`() {
        val allenKey = make(an(allenKey).with(color, Color.RED))

        assertThat("color", allenKey.color, equalTo(Color.RED))
    }

    @Test
    fun `how to use an alternate syntax`() {
        val pliers = make(some(pliers, with(color, Color.RED)))

        assertThat("color", pliers.color, equalTo(Color.RED))
    }

    @Test
    fun `how to use composite properties, aka virtual properties`() {
        val flatHeaded = make(a(screwDriver, with(flatHead, Color.YELLOW)))

        assertThat("head", flatHeaded.head, equalTo(Head.FLAT))
        assertThat("color", flatHeaded.color, equalTo(Color.YELLOW))
    }
}