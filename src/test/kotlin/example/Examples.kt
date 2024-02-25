package example

import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import com.vtence.konstruct.*
import example.AllenKeyMaker.allenKey
import example.HouseholdToolMaker.color
import example.PliersMaker.pliers
import example.ScrewDriverMaker.flatHead
import example.ScrewDriverMaker.head
import example.ScrewDriverMaker.screwDriver
import example.ScrewHead.FLAT
import example.ScrewHead.PHILIPS
import example.TapeMaker.tape
import example.ToolBagMaker.toolbag
import example.ToolBagMaker.tools
import example.ToolHolderMaker.holder
import example.ToolHolderMaker.tool
import org.junit.jupiter.api.Test


class Examples {
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
        val flatHeaded = make(a(screwDriver).with(flatHead, Color.YELLOW))

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
        val bag = make(
            a(
                toolbag, with(
                    tools, aListOf(
                        a(screwDriver, with(head, FLAT)),
                        a(screwDriver, with(head, PHILIPS))
                    )
                )
            )
        )

        val heads = bag.tools.map { (it as? ScrewDriver)?.head }
        assertThat("heads", heads, equalTo(listOf(FLAT, PHILIPS)))
    }

    @Test
    fun `how to use fabricators instead of factories`() {
        val fruit = make(a(banana).with(banana.ripeness, 0.8f))

        assertThat("ripeness", fruit.ripeness, equalTo(0.8f))
    }

    @Test
    fun `an alternate way to use fabricators `() {
        val fruit = make(a(banana) {
            with(it.ripeness, 0.8f)
        })

        assertThat("ripeness", fruit.ripeness, equalTo(0.8f))
    }
}