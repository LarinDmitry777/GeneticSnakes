import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
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

    @Test
    fun `toDuque test int`() {
        val l = listOf(1, 2, 3, 4 ,5)
        val deque = l.toDeque()
        //ToDo Как узнать класс не создавая экземпляра объекта
        assertEquals(ArrayDeque<Int>().javaClass, deque.javaClass)
        assertEquals(l, deque.toList())
    }

    @Test
    fun `toDeque test strings`() {
        val l = listOf("1", "2", "3", "4", '5')
        val deque = l.toDeque()
        assertEquals(ArrayDeque<String>().javaClass, deque.javaClass)
        assertEquals(l, deque.toList())
    }

    @Test
    fun `test toDeque empty`(){
        val l = listOf<Char>()
        val d = l.toDeque()
        assertEquals(ArrayDeque<Char>().javaClass, d.javaClass)
        assertEquals(0, d.size)
    }
}