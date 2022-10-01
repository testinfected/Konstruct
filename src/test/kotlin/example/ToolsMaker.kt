package example

import com.vtence.konstruct.Factory
import com.vtence.konstruct.define
import com.vtence.konstruct.property
import example.HouseholdToolMaker.color
import example.HouseholdToolMaker.name

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
    val flatHead = define<ScrewDriver, Color> { with(head, ScrewHead.FLAT).with(color, it) }

    val screwDriver = Factory {
        ScrewDriver(
            name = it.valueOf(name) ?: "no name",
            color = it.valueOf(color) ?: Color.RED,
            head = it.valueOf(head) ?: ScrewHead.FLAT
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

