package logic

import java.io.Serializable

class Algorithm : Serializable {

    companion object {
        private const val sensorMatrixSize = 11;
        private const val snakeViewRadius = 5;
    }


    private fun createByteMatrix(size: Int = sensorMatrixSize): Array<Array<Byte>> {
        return Array(size) { Array(size) { 0.toByte() } }
    }

    private fun createHashMapForSensors(): HashMap<Direction, Array<Array<Byte>>> {
        return hashMapOf(
            Direction.TOP to createByteMatrix(),
            Direction.LEFT to createByteMatrix(),
            Direction.BOT to createByteMatrix(),
            Direction.RIGHT to createByteMatrix()
        )
    }

    val foodSensors = createHashMapForSensors()
    val wallSensors = createHashMapForSensors()

    fun getFoodSensors(direction: Direction): Array<Array<Byte>> {
        return foodSensors[direction]!!
        //ToDO Почему он может быть null
    }

    fun getWallSensors(direction: Direction): Array<Array<Byte>> {
        return wallSensors[direction]!!
    }

    fun correctSensors(walls: Iterable<Point>, foods: Iterable<Point>, direction: Direction, snakeHeadPosition: Point) {
        fun correctSensor(objects: Iterable<Point>, sensor: Array<Array<Byte>>, isCorrectMove: Boolean) {
            objects.mapNotNull { it.toSnakeSensorsPoint(snakeHeadPosition) }
                .forEach { point ->
                    if (isCorrectMove)
                        sensor[point.x][point.y]++ //ToDo как сделать через += тернарка
                    else
                        sensor[point.x][point.y]--
                }

        }

        val expectedSnakeDirection = generateDirection(walls, foods, snakeHeadPosition)
        correctSensor(walls, wallSensors[direction]!!, true)
        correctSensor(foods, foodSensors[direction]!!, true)

        if (direction != expectedSnakeDirection) {
            correctSensor(walls, wallSensors[expectedSnakeDirection]!!, false)
            correctSensor(foods, foodSensors[expectedSnakeDirection]!!, false)
        }
    }

    fun generateDirection(walls: Iterable<Point>, food: Iterable<Point>, snakeHeadPosition: Point): Direction {
        fun addValuesFromSensors(
            directionPrivilege: MutableMap<Direction, Byte>,
            objectCoords: Iterable<Point>,
            sensors: HashMap<Direction, Array<Array<Byte>>>
        ) {
            objectCoords.mapNotNull { it.toSnakeSensorsPoint(snakeHeadPosition) }
                .forEach { point ->
                    Direction.values().forEach { direction ->
                        directionPrivilege[direction] =
                            (directionPrivilege[direction]!! + sensors[direction]!![point.x][point.y]).toByte()
                        // ToDO Not null и +=
                    }
                }
        }

        val directionPrivilege = mutableMapOf<Direction, Byte>()
        for (direction in Direction.values())
            directionPrivilege[direction] = 0.toByte()

        addValuesFromSensors(directionPrivilege, walls, wallSensors)
        addValuesFromSensors(directionPrivilege, food, foodSensors)

        return directionPrivilege.toList().maxBy { it.second }!!.first
    }

    private fun Point.toSnakeSensorsPoint(snakeHeadPosition: Point): Point? {
        val sensorX = this.x - snakeHeadPosition.x + logic.Algorithm.Companion.snakeViewRadius
        val sensorY = this.y - snakeHeadPosition.y + logic.Algorithm.Companion.snakeViewRadius
        if (sensorX in 1 until logic.Algorithm.Companion.sensorMatrixSize && sensorY in 1 until logic.Algorithm.Companion.sensorMatrixSize)
            return Point(sensorX, sensorY)
        return null
    }


}