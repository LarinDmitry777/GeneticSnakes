package logic

import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.abs
import kotlin.math.max

data class Algorithm(var num: Int = 1) : Serializable {

    companion object {
        const val sensorMatrixSize = 11;
        private const val snakeViewRadius = 5;


        fun generateRandomAlgorithm(antiKamikaze: Boolean = true, peekNearFood: Boolean = true): Algorithm {
            val algorithm = Algorithm()
            val points = mutableListOf<Point>()
            for (y in 0 until 11)
                for (x in 0 until 11) {
                    points.add(Point(x, y))
                }
            Direction.values().forEach { direction ->
                points.forEach { point ->
                    algorithm.foodSensors[direction]!![point.y][point.x] = Random().nextInt(10) - 5
                    algorithm.wallSensors[direction]!![point.y][point.x] = Random().nextInt(10) - 5
                }
            }
            if (antiKamikaze) {
                algorithm.wallSensors[Direction.LEFT]!![5][4] = -25
                algorithm.wallSensors[Direction.RIGHT]!![5][6] = -25
                algorithm.wallSensors[Direction.TOP]!![4][5] = -25
                algorithm.wallSensors[Direction.BOT]!![6][5] = -25
            }
            if (peekNearFood) {
                algorithm.foodSensors[Direction.LEFT]!![5][4] = 25
                algorithm.foodSensors[Direction.RIGHT]!![5][6] = 25
                algorithm.foodSensors[Direction.TOP]!![4][5] = 25
                algorithm.foodSensors[Direction.BOT]!![6][5] = 25
            }
            return algorithm
        }

        fun mutate(oldAlgorithm: Algorithm): Algorithm {
            val r = Random()
            val newAlgorithm = Algorithm()
            Direction.values().forEach { direction ->
                for (y in 0 until sensorMatrixSize)
                    for (x in 0 until sensorMatrixSize) {
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
                val x = r.nextInt(sensorMatrixSize)
                val y = r.nextInt(sensorMatrixSize)
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

    //ToDo методы над полями
    private fun createMatrix(size: Int = sensorMatrixSize): Array<Array<Int>> {
        return Array(size) { Array(size) { 0 } }
    }

    private fun createHashMapForSensors(): HashMap<Direction, Array<Array<Int>>> {
        return hashMapOf(
            Direction.TOP to createMatrix(),
            Direction.LEFT to createMatrix(),
            Direction.BOT to createMatrix(),
            Direction.RIGHT to createMatrix()
        )
    }

    fun correctSensors(walls: Iterable<Point>, foods: Iterable<Point>, direction: Direction, snakeHeadPosition: Point) {
        fun correctSensor(objects: Iterable<Point>, sensor: Array<Array<Int>>, isCorrectMove: Boolean) {
            objects.mapNotNull { it.toSnakeSensorsPoint(snakeHeadPosition) }
                .forEach { point ->
                    if (isCorrectMove)
                        sensor[point.y][point.x]++ //ToDo как сделать через += тернарка
                    else
                        sensor[point.y][point.x]--
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
            directionPrivilege: MutableMap<Direction, Double>,
            objectCoords: Iterable<Point>,
            sensors: HashMap<Direction, Array<Array<Int>>>
        ) {
            objectCoords.mapNotNull { it.toSnakeSensorsPoint(snakeHeadPosition) }
                .forEach { point ->
                    if (point != Point(5, 5))
                        Direction.values().forEach { direction ->
                            val oldValue = directionPrivilege[direction]!!.toDouble()
                            val diff = sensors[direction]!![point.y][point.x].toDouble()
                            val divide =
                                (max(abs(point.x - snakeViewRadius), abs(point.y - snakeViewRadius))).toDouble()
                            val newDiff = diff / divide
                            val newValue = oldValue + newDiff
                            directionPrivilege[direction] = newValue
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
        val sensorX = this.x - snakeHeadPosition.x + snakeViewRadius
        val sensorY = this.y - snakeHeadPosition.y + snakeViewRadius
        if (sensorX in 1 until sensorMatrixSize && sensorY in 1 until sensorMatrixSize)
            return Point(sensorX, sensorY)
        return null
    }


}