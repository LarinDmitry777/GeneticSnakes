import java.util.*

fun <T> List<T>.toDeque(): ArrayDeque<T> {
    val deque = ArrayDeque<T>()
    this.forEach { deque.add(it) }
    return deque
}

fun getRandomNumberInRange(start: Int, end: Int, isNeedZero: Boolean = true): Int {
    var value = Random().nextInt(end - start) + start
    while (value == 0 && !isNeedZero)
        value = Random().nextInt(end - start) + start
    return value
}