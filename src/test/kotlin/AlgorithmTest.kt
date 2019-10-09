import logic.Algorithm
import logic.Direction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class HelloJunit5Test {
    @Test
    fun `InitWithEmptySensors`() {
        val algorithm = Algorithm()
        for (direction in Direction.values()) {
            algorithm.getFoodSensors(direction).forEach { raw -> raw.forEach { assertEquals(0, it) } }
            algorithm.getWallSensors(direction).forEach { raw -> raw.forEach { assertEquals(0, it) } }
        }
    }

    @Test
    fun `OneFoodLeftCorrectTest`() {
        val algorithm = Algorithm()
        for (direction in Direction.values()) {
            algorithm.getFoodSensors(direction).forEach { raw -> raw.forEach { assertEquals(0, it) } }
            algorithm.getWallSensors(direction).forEach { raw -> raw.forEach { assertEquals(0, it) } }
        }
    }
}