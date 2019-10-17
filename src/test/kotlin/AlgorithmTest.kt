import logic.Algorithm
import logic.Config
import logic.Direction
import logic.Direction.*
import logic.Point
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.min


class AlgorithmTests {
    @RepeatedTest(3)
    fun `Test sensors values in range of random algorithm`() {
        val randomAlgorithm = Algorithm.generateRandomAlgorithm(antiKamikaze = false, peekNearFood = false)
        var isMaxValueFoodExist = false
        var isMaxValueWallExist = false
        var isMinValueFoodExist = false
        var isMinValueWallExist = false
        values().forEach { direction ->
            for (x in 0 until Algorithm.SENSOR_MATRIX_SIZE)
                for (y in 0 until Algorithm.SENSOR_MATRIX_SIZE) {
                    val wallSensorValue = randomAlgorithm.wallSensors[direction]!![y][x]
                    val foodSensorValue = randomAlgorithm.foodSensors[direction]!![y][x]
                    if (wallSensorValue == Config.ALGORITHM_SENSOR_DISPERSION)
                        isMaxValueWallExist = true
                    if (wallSensorValue == -Config.ALGORITHM_SENSOR_DISPERSION)
                        isMinValueWallExist = true
                    if (foodSensorValue == Config.ALGORITHM_SENSOR_DISPERSION)
                        isMaxValueFoodExist = true
                    if (foodSensorValue == -Config.ALGORITHM_SENSOR_DISPERSION)
                        isMinValueFoodExist = true

                    assertTrue(wallSensorValue <= Config.ALGORITHM_SENSOR_DISPERSION)
                    assertTrue(wallSensorValue >= -Config.ALGORITHM_SENSOR_DISPERSION)
                    assertTrue(foodSensorValue <= Config.ALGORITHM_SENSOR_DISPERSION)
                    assertTrue(foodSensorValue >= -Config.ALGORITHM_SENSOR_DISPERSION)

                }
            assertTrue(isMaxValueFoodExist)
            assertTrue(isMaxValueWallExist)
            assertTrue(isMinValueWallExist)
            assertTrue(isMinValueFoodExist)
        }
    }

    @RepeatedTest(100)
    fun `Test random algorithm anti kamikaze one wall`() {
        val algorihtm = Algorithm.generateRandomAlgorithm(antiKamikaze = true, peekNearFood = false)
        val snakeHead = Point(5, 5)
        values().forEach { direction ->
            val food = listOf<Point>()
            val walls = listOf(direction.toOffset() + snakeHead)
            val directionToMove = algorihtm.generateDirection(walls, food, snakeHead)
            assertFalse(directionToMove == direction)
        }
    }

    @RepeatedTest(100)
    fun `Test random algorithm anti kamikaze three wall`() {
        val algorithm = Algorithm.generateRandomAlgorithm(antiKamikaze = true, peekNearFood = false)
        val snakeHead = Point(5, 5)
        values().forEach { direction ->
            val food = listOf<Point>()
            val walls = values()
                .filter { it != direction }
                .map { it.toOffset() + snakeHead }
                .toList()
            val directionToMove = algorithm.generateDirection(walls, food, snakeHead)
            assertEquals(direction, directionToMove)
        }
    }

    @RepeatedTest(100)
    fun `Test random algorithm peek near food one food`() {
        val algorithm = Algorithm.generateRandomAlgorithm(antiKamikaze = false, peekNearFood = true)
        val snakeHead = Point(5, 5)
        val walls = listOf<Point>()
        values().forEach { direction ->
            val food = listOf(direction.toOffset() + snakeHead)
            val directionToMove = algorithm.generateDirection(walls, food, snakeHead)
            assertEquals(direction, directionToMove)

        }
    }

    fun setAlgorithmSensorsFixedValues(algorithm: Algorithm, value: Int) {
        for (x in 0 until 11)
            for (y in 0 until 11) {
                values().forEach { direction ->
                    algorithm.foodSensors[direction]!![y][x] = value
                    algorithm.wallSensors[direction]!![y][x] = value
                }
            }
    }

    fun setAlgorithmSensorsCrossValues(algorithm: Algorithm, value: Int) {
        for (x in 0 until 11)
            for (y in 0 until 11) {
                if (x != 5 || y != 5) {
                    if (x == 5) {
                        if (y < 5) {
                            algorithm.foodSensors[TOP]!![y][x] = value
                            algorithm.wallSensors[TOP]!![y][x] = -value
                        }
                        if (y > 5) {
                            algorithm.foodSensors[BOT]!![y][x] = value
                            algorithm.wallSensors[BOT]!![y][x] = -value
                        }
                    }
                    if (y == 5) {
                        if (x < 5) {
                            algorithm.foodSensors[LEFT]!![y][x] = value
                            algorithm.wallSensors[LEFT]!![y][x] = -value
                        }
                        if (x > 5) {
                            algorithm.foodSensors[RIGHT]!![y][x] = value
                            algorithm.wallSensors[RIGHT]!![y][x] = -value
                        }
                    }
                }
            }
    }


    @RepeatedTest(100)
    fun `Test algorithm get direction food in distance`() {
        val algorithm = Algorithm()
        setAlgorithmSensorsCrossValues(algorithm, 100)

        val trueDirection = values().random()
        val falseDirection = trueDirection.inverse()
        val trueDistance = getRandomNumberInRange(1, 4)
        val falseDistance = getRandomNumberInRange(trueDistance + 1, 5)


        val truePoint = trueDirection.toOffset() * trueDistance
        val falsePoint = falseDirection.toOffset() * falseDistance


        val food = listOf(truePoint, falsePoint)
        val walls = listOf<Point>()
        val direction = algorithm.generateDirection(walls, food, Point(0, 0))
        assertEquals(trueDirection, direction)
    }

    @RepeatedTest(10)
    fun `test food behind the wall cross algorithm`() {
        val algorithm = Algorithm()
        setAlgorithmSensorsCrossValues(algorithm, 100)
        Direction.values().forEach { direction ->
            val wallPoint = direction.toOffset() + Algorithm.MATRIX_CENTER
            val foodPoint = wallPoint + direction.toOffset()
            val walls = listOf(wallPoint)
            val food = listOf(foodPoint)
            val snakeDirection = algorithm.generateDirection(walls, food, Algorithm.MATRIX_CENTER)
            assertFalse(direction == snakeDirection)
        }
    }

    @RepeatedTest(3)
    fun `test algorithm clone`() {
        val oldAlgorithm = Algorithm.generateRandomAlgorithm()
        val newAlgorithm = oldAlgorithm.clone()

        assertFalse(oldAlgorithm.wallSensors === newAlgorithm.wallSensors)
        assertFalse(oldAlgorithm.foodSensors === newAlgorithm.foodSensors)

        Direction.values().forEach { direction ->
            assertFalse(oldAlgorithm.wallSensors[direction] === newAlgorithm.wallSensors[direction])
            assertFalse(oldAlgorithm.foodSensors[direction] === newAlgorithm.foodSensors[direction])

            for (y in 0 until Algorithm.SENSOR_MATRIX_SIZE) {
                assertFalse(oldAlgorithm.wallSensors[direction]!![y] === newAlgorithm.wallSensors[direction]!![y])
                assertFalse(oldAlgorithm.foodSensors[direction]!![y] === newAlgorithm.foodSensors[direction]!![y])

                for (x in 0 until Algorithm.SENSOR_MATRIX_SIZE) {
                    assertEquals(
                        oldAlgorithm.wallSensors[direction]!![y][x],
                        newAlgorithm.wallSensors[direction]!![y][x]
                    )
                    assertEquals(
                        oldAlgorithm.foodSensors[direction]!![y][x],
                        newAlgorithm.foodSensors[direction]!![y][x]
                    )
                }
            }
        }
    }

    @Test
    fun `Test mutation count`() {
        for (i in 0..100) {
            val mutateCount = i
            val algorithm = Algorithm.generateRandomAlgorithm()
            val newAlgorithm = algorithm.getMutatedClone(mutateCount = mutateCount)
            var diffCount = 0
            Direction.values().forEach { direction ->
                for (x in 0 until Algorithm.SENSOR_MATRIX_SIZE)
                    for (y in 0 until Algorithm.SENSOR_MATRIX_SIZE) {
                        if (algorithm.foodSensors[direction]!![y][x] != newAlgorithm.foodSensors[direction]!![y][x]) diffCount++
                        if (algorithm.wallSensors[direction]!![y][x] != newAlgorithm.wallSensors[direction]!![y][x]) diffCount++
                    }
            }
            assertEquals(mutateCount, diffCount)
        }
    }
}

