package gui

import java.io.Serializable

enum class FieldElement: Serializable {
    SNAKE_HEAD, SNAKE_BODY, WALL, EMPTY_CELL, FOOD;

    fun isImpassable(): Boolean {
        return when (this) {
            FOOD, EMPTY_CELL -> false
            else -> true
        }
    }
}