@file:JvmName("Main")

import gui.ConsoleGraphics
import gui.FieldElement
import logic.FieldCell
import logic.Point

fun main() {
    println()
    ConsoleGraphics.generateField(10, 5)
    val w = FieldElement.WALL
    val h = FieldElement.SNAKE_HEAD
    val s = FieldElement.SNAKE_BODY
    val e = FieldElement.EMPTY_CELL


    val field = listOf(listOf(e, e, e), listOf(s, s, s), listOf(e, e, h))
    ConsoleGraphics.drawField(field)
    ConsoleGraphics.updatedElements(listOf(FieldCell(w, Point(0, 1))) as Iterable<FieldCell>)
}