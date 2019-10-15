package gui

import java.util.*

fun <T>List<T>.toDeque (): ArrayDeque<T> {
    val deque = ArrayDeque<T>()
    this.forEach { deque.add(it) }
    return deque
}