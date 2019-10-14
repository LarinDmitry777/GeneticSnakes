package logic

import java.util.*

class Snake(val cells: ArrayDeque<Point>,
            val algorithm: Algorithm,
            private val energyForFood: Int) {

    var energy = energyForFood

    fun getHeadPosition(): Point = cells.first

    fun getMoveDirection(walls: Iterable<Point>, food: Iterable<Point>): Direction {
        val moveTo = algorithm.generateDirection(walls, food, getHeadPosition())
        return moveTo
    }

    fun correctSensors(walls: Iterable<Point>, food: Iterable<Point>, direction: Direction) {
        algorithm.correctSensors(walls, food, direction, getHeadPosition())
    }

    fun move(direction: Direction, isEatFood: Boolean) {
        val offset = direction.getOffset()
        cells.addFirst(getHeadPosition() + offset)
        if (!isEatFood)
            cells.removeLast()
        energy--
        if (energy <= 0 && cells.count() > 4){
            cells.removeLast()
            energy = energyForFood
        }
        if (isEatFood)
            energy += energyForFood
    }
}