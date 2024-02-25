package example

import com.vtence.konstruct.*

class Banana(val ripeness: Float)

object banana: Fabricator<Banana> {
    val ripeness = property<Banana, Float>()

    override val factory = Factory {
        Banana(it[ripeness] ?: 0.5f)
    }
}
