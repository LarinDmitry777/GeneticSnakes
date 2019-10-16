import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.RepeatedTest
import java.util.*
import kotlin.collections.RandomAccess


class UtilTest {
    @RepeatedTest(3)
    fun `test getRandomNumberInRange`() {
        val start = Random().nextInt(10) - 10
        val end = Random().nextInt(10)
        var isStartExist = false
        var isEndExist = false

        for (i in 0 until 1000) {
            val value = getRandomNumberInRange(start, end)
            assertTrue(value <= end)
            assertTrue(value >= start)
            if (value == start) isStartExist = true
            if (value == end) isEndExist = true
            if (isEndExist && isStartExist)
                break
        }

    }
}