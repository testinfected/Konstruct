package com.vtence.konstruct

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import com.vtence.konstruct.AllenKeyMaker.allenKey
import com.vtence.konstruct.Konstruct.aListOf
import com.vtence.konstruct.Konstruct.having
import com.vtence.konstruct.Konstruct.make
import com.vtence.konstruct.Konstruct.with
import com.vtence.konstruct.HouseholdToolMaker.color
import com.vtence.konstruct.HouseholdToolMaker.name
import com.vtence.konstruct.PliersMaker.pliers
import com.vtence.konstruct.Property.Companion.property
import com.vtence.konstruct.ScrewDriverMaker.flatHead
import com.vtence.konstruct.ScrewDriverMaker.screwDriver
import com.vtence.konstruct.TapeMaker.tape
import com.vtence.konstruct.Maker.Companion.a
import com.vtence.konstruct.Maker.Companion.an
import com.vtence.konstruct.Maker.Companion.some
import com.vtence.konstruct.ScrewDriverMaker.head
import com.vtence.konstruct.ScrewHead.*
import com.vtence.konstruct.ToolBagMaker.toolbag
import com.vtence.konstruct.ToolBagMaker.tools
import com.vtence.konstruct.ToolHolderMaker.holder
import com.vtence.konstruct.ToolHolderMaker.tool
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
    val tape = Factory {
        Tape(
            name = it.valueOf(name) ?: "standard tape",
            color = it.valueOf(color) ?: Color.YELLOW
        )
    }
}

class AllenKey(name: String, color: Color) : HouseholdTool(name, color)

object AllenKeyMaker {
    val allenKey = Factory {
        AllenKey(
            name = it.valueOf(name) ?: "standard hex key",
            color = it.valueOf(color) ?: Color.BLACK
        )
    }
}

class Pliers(name: String, color: Color) : HouseholdTool(name, color)

object PliersMaker {
    val pliers = Factory {
        Pliers(
            name = it.valueOf(name) ?: "standard pliers",
            color = it.valueOf(color) ?: Color.BLUE
        )
    }
}


enum class ScrewHead {
    PHILIPS, FLAT
}

class ScrewDriver(name: String, color: Color, val head: ScrewHead): HouseholdTool(name, color)

object ScrewDriverMaker {
    val head = property<ScrewDriver, ScrewHead>()
    val flatHead = property<ScrewDriver, Color> { with(head, FLAT).with(color, it) }

    val screwDriver = Factory {
        ScrewDriver(
            name = it.valueOf(name) ?: "no name",
            color = it.valueOf(color) ?: Color.RED,
            head = it.valueOf(head) ?: FLAT
        )
    }
}


class ToolHolder(
    val tool: HouseholdTool?
)

object ToolHolderMaker {
    val tool = property<ToolHolder, HouseholdTool?>()

    val holder = Factory {
        ToolHolder(it.valueOf(tool))
    }
}

class ToolBag(
    val tools: Iterable<HouseholdTool>
)

object ToolBagMaker {
    val tools = property<ToolBag, Iterable<HouseholdTool>>()

    val toolbag = Factory {
        ToolBag(it.valueOf(tools) ?: emptyList())
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

        assertThat("pliers color", pliers.color, equalTo(Color.RED))

        val tape = make(a(tape, having(color, Color.BLACK)))

        assertThat("tape color", tape.color, equalTo(Color.BLACK))
    }

    @Test
    fun `how to use composite properties, aka virtual properties`() {
        val flatHeaded = make(a(screwDriver, with(flatHead, Color.YELLOW)))

        assertThat("head", flatHeaded.head, equalTo(FLAT))
        assertThat("color", flatHeaded.color, equalTo(Color.YELLOW))
    }

    @Test
    fun `how to use makers as property values`() {
        val holder = make(a(holder, with(tool, a(screwDriver, with(head, PHILIPS)))))

        assertThat("tool", (holder.tool as? ScrewDriver)?.head, present(equalTo(PHILIPS)))
    }

    @Test
    fun `how to specify null property values`() {
        val holder = make(a(holder).withNull(tool))

        assertThat("tool", holder.tool, absent())
    }

    @Test
    fun `how to specify a list of property values`() {
        val bag = make(a(toolbag, with(tools, aListOf(
            a(screwDriver, with(head, FLAT)),
            a(screwDriver, with(head, PHILIPS))
        ))))

        val heads = bag.tools.map { (it as? ScrewDriver)?.head }
        assertThat("heads", heads, equalTo(listOf(FLAT, PHILIPS)))
    }
}