package logic

data class Point(val x: Int, val y: Int) {
    operator fun plus(point: Point): Point {
        return Point(this.x + point.x, this.y + point.y)
    }

}