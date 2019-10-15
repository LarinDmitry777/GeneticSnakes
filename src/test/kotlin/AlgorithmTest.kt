import logic.Algorithm
import logic.Config
import logic.Direction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class AlgorithmTests {
    @Test
    fun `test random algorithm create sensors values range`() {
        val randomAlgorithm = Algorithm.generateRandomAlgorithm(antiKamikaze = false, peekNearFood = false)
        var isMaxValueFoodExist = false
        var isMaxValueWallExist = false
        var isMinValueFoodExist = false
        var isMinValueWallExist = false
        Direction.values().forEach { direction ->
            for (x in 0 until Algorithm.sensorMatrixSize)
                for (y in 0 until Algorithm.sensorMatrixSize) {
                    val wallSensorValue = randomAlgorithm.wallSensors[direction]!![y][x]
                    val foodSensorValue = randomAlgorithm.foodSensors[direction]!![y][x]
                    if (wallSensorValue == Config.algorithmSensorsDispersion)
                        isMaxValueWallExist = true
                    if (wallSensorValue == -Config.algorithmSensorsDispersion)
                        isMinValueWallExist = true
                    if (foodSensorValue == Config.algorithmSensorsDispersion)
                        isMaxValueFoodExist = true
                    if (foodSensorValue == -Config.algorithmSensorsDispersion)
                        isMaxValueFoodExist = true

                    assertTrue(wallSensorValue <= Config.algorithmSensorsDispersion)
                    assertTrue(wallSensorValue >= Config.algorithmSensorsDispersion)
                    assertTrue(foodSensorValue >= Config.algorithmSensorsDispersion)
                    assertTrue(foodSensorValue >= Config.algorithmSensorsDispersion)

                }
            assertTrue(isMaxValueFoodExist)
            assertTrue(isMaxValueWallExist)
            assertTrue(isMinValueWallExist)
            assertTrue(isMinValueFoodExist)
        }
    }

}