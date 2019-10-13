package logic

enum class Direction {
    TOP, BOT, LEFT, RIGHT;

    fun getOffset(): Point {
        return when (this) {
            TOP -> Point(0, 1)
            BOT -> Point(0, -1)
            LEFT -> Point(-1, 0)
            RIGHT -> Point(1, 0)
        }
    }
}