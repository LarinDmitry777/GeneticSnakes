package logic

enum class Direction {
    TOP, BOT, LEFT, RIGHT;

    fun toOffset(): Point {
        return when (this) {
            TOP -> Point(0, -1)
            BOT -> Point(0, 1)
            LEFT -> Point(-1, 0)
            RIGHT -> Point(1, 0)
        }
    }

    fun inverse(): Direction {
        return when (this) {
            TOP -> BOT
            BOT -> TOP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}