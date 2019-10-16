import java.util.*

fun <T> List<T>.toDeque(): ArrayDeque<T> {
    val deque = ArrayDeque<T>()
    this.forEach { deque.add(it) }
    return deque
}

fun getRandomNumberInRange(start: Int, end: Int): Int {
    return Random().nextInt(end - start) + start
}