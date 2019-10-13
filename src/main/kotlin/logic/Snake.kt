package logic

import java.util.*

class Snake(val cells: ArrayDeque<Point>, val algorithm: Algorithm) {
    val headPosition: Point = cells.first

    fun getMoveDirection(walls: Iterable<Point>, food: Iterable<Point>) {
        algorithm.generateDirection(walls, food, headPosition)
    }

    fun correctSensors(walls: Iterable<Point>, food: Iterable<Point>, direction: Direction) {
        algorithm.correctSensors(walls, food, direction, headPosition)
    }

    fun move(direction: Direction, isEatFood: Boolean) {
        val offset = direction.getOffset()
        cells.addFirst(headPosition + offset)
        if (!isEatFood)
            cells.removeLast()
    }
}