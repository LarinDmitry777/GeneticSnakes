package logic

import java.io.Serializable
import java.util.*

class Snake(
    val cells: ArrayDeque<Point>,
    val algorithm: Algorithm,
    private val energyForFood: Int
): Serializable {

    var energy = energyForFood
    var lifeTicksCount = 0

    val headPosition: Point
        get() = cells.first


    fun getMoveDirection(walls: Iterable<Point>, food: Iterable<Point>): Direction {
        val moveTo = algorithm.generateDirection(walls, food, headPosition)
        return moveTo
    }

    fun move(direction: Direction, isEatFood: Boolean) {
        lifeTicksCount++
        val offset = direction.toOffset()
        cells.addFirst(headPosition + offset)
        if (!isEatFood)
            cells.removeLast()
        energy--
        if (energy <= 0 && cells.count() > 4) {
            cells.removeLast()
            energy = energyForFood
        }
        if (isEatFood)
            energy += energyForFood
    }
}