package logic

import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.math.max

data class Algorithm(var num: Int = 1) : Serializable {

    companion object {
        const val SNAKE_VIEW_RADIUS = 5
        const val SENSOR_MATRIX_SIZE = SNAKE_VIEW_RADIUS * 2 + 1
        val MATRIX_CENTER = Point(SNAKE_VIEW_RADIUS, SNAKE_VIEW_RADIUS)


        fun generateRandomAlgorithm(antiKamikaze: Boolean = true, peekNearFood: Boolean = true): Algorithm {
            val algorithm = Algorithm()
            val random = Random()

            for (x in 0 until SENSOR_MATRIX_SIZE)
                for (y in 0 until SENSOR_MATRIX_SIZE) {
                    Direction.values().forEach { direction ->
                        algorithm.foodSensors[direction]!![y][x] =
                            random.nextInt(Config.ALGORITHM_SENSOR_DISPERSION * 2 + 1) - Config.ALGORITHM_SENSOR_DISPERSION
                        algorithm.wallSensors[direction]!![y][x] =
                            random.nextInt(Config.ALGORITHM_SENSOR_DISPERSION * 2 + 1) - Config.ALGORITHM_SENSOR_DISPERSION
                    }
                }

            if (antiKamikaze || peekNearFood) {
                val matrixCenter = Point(SNAKE_VIEW_RADIUS, SNAKE_VIEW_RADIUS)
                Direction.values()
                    .forEach { direction ->
                        val cellsCoordinates = direction.toOffset() + matrixCenter
                        if (antiKamikaze)
                            algorithm.wallSensors[direction]!![cellsCoordinates.y][cellsCoordinates.x] =
                                -Config.ALGORITHM_RANDOM_FIXED_CELLS_VALUE
                        if (peekNearFood)
                            algorithm.foodSensors[direction]!![cellsCoordinates.y][cellsCoordinates.x] =
                                Config.ALGORITHM_RANDOM_FIXED_CELLS_VALUE
                    }
            }
            return algorithm
        }

        fun mutate(oldAlgorithm: Algorithm): Algorithm {
            val r = Random()
            val newAlgorithm = Algorithm()
            Direction.values().forEach { direction ->
                for (y in 0 until SENSOR_MATRIX_SIZE)
                    for (x in 0 until SENSOR_MATRIX_SIZE) {
                        newAlgorithm.wallSensors[direction]!![y][x] =
                            oldAlgorithm.wallSensors[direction]!![y][x]
                        newAlgorithm.foodSensors[direction]!![y][x] =
                            oldAlgorithm.foodSensors[direction]!![y][x]
                    }
            }
            newAlgorithm.num = oldAlgorithm.num + 1
            for (i in 0..r.nextInt(7)) {
                val valueForMutate = r.nextInt(10) - 5
                val directionForMutate = Direction.values().toList().random()
                val x = r.nextInt(SENSOR_MATRIX_SIZE)
                val y = r.nextInt(SENSOR_MATRIX_SIZE)
                val isWallSensorMutate = r.nextBoolean()
                if (isWallSensorMutate)
                    newAlgorithm.wallSensors[directionForMutate]!![y][x] =
                        newAlgorithm.wallSensors[directionForMutate]!![y][x] + valueForMutate
                else
                    newAlgorithm.foodSensors[directionForMutate]!![y][x] =
                        newAlgorithm.foodSensors[directionForMutate]!![y][x] + valueForMutate
            }
            return newAlgorithm
        }
    }

    val foodSensors = createHashMapForSensors()
    val wallSensors = createHashMapForSensors()

    private fun createHashMapForSensors(): HashMap<Direction, Array<Array<Int>>> {
        fun createMatrix() = Array(SENSOR_MATRIX_SIZE) { Array(SENSOR_MATRIX_SIZE) { 0 } }
        return hashMapOf(
            Direction.TOP to createMatrix(),
            Direction.LEFT to createMatrix(),
            Direction.BOT to createMatrix(),
            Direction.RIGHT to createMatrix()
        )
    }


    fun generateDirection(walls: Iterable<Point>, food: Iterable<Point>, snakeHeadPosition: Point): Direction {
        fun addValuesFromSensors(
            directionPrivileges: MutableMap<Direction, Double>,
            objects: Iterable<Point>,
            sensors: HashMap<Direction, Array<Array<Int>>>
        ) {
            objects.mapNotNull { it.toSnakeSensorsPoint(snakeHeadPosition) }
                .forEach { point ->
                    if (point != MATRIX_CENTER)
                        Direction.values().forEach { direction ->
                            val oldValue = directionPrivileges[direction]!!
                            val sensorValue = sensors[direction]!![point.y][point.x].toDouble()
                            val divide =
                                (max(abs(point.x - SNAKE_VIEW_RADIUS), abs(point.y - SNAKE_VIEW_RADIUS))).toDouble()
                            val newDiff = sensorValue / divide
                            val newValue = oldValue + newDiff
                            directionPrivileges[direction] = newValue
                            // ToDO +=
                        }
                }
        }

        val directionPrivilege = mutableMapOf<Direction, Double>()
        Direction.values().forEach { directionPrivilege[it] = 0.toDouble() }

        addValuesFromSensors(directionPrivilege, walls, wallSensors)
        addValuesFromSensors(directionPrivilege, food, foodSensors)
        return directionPrivilege.toList().maxBy { it.second }!!.first
    }

    private fun Point.toSnakeSensorsPoint(snakeHeadPosition: Point): Point? {
        val sensorX = this.x - snakeHeadPosition.x + SNAKE_VIEW_RADIUS
        val sensorY = this.y - snakeHeadPosition.y + SNAKE_VIEW_RADIUS
        if (sensorX in 1 until SENSOR_MATRIX_SIZE && sensorY in 1 until SENSOR_MATRIX_SIZE)
            return Point(sensorX, sensorY)
        return null
    }


}

//    fun correctSensors(walls: Iterable<Point>, foods: Iterable<Point>, direction: Direction, snakeHeadPosition: Point) {
////        fun correctSensor(objects: Iterable<Point>, sensor: Array<Array<Int>>, isCorrectMove: Boolean) {
////            objects.mapNotNull { it.toSnakeSensorsPoint(snakeHeadPosition) }
////                .forEach { point ->
////                    if (isCorrectMove)
////                        sensor[point.y][point.x]++ //ToDo как сделать через += тернарка
////                    else
////                        sensor[point.y][point.x]--
////                }
////        }
////
////        val expectedSnakeDirection = generateDirection(walls, foods, snakeHeadPosition)
////        correctSensor(walls, wallSensors[direction]!!, true)
////        correctSensor(foods, foodSensors[direction]!!, true)
////
////        if (direction != expectedSnakeDirection) {
////            correctSensor(walls, wallSensors[expectedSnakeDirection]!!, false)
////            correctSensor(foods, foodSensors[expectedSnakeDirection]!!, false)
////        }
////    }