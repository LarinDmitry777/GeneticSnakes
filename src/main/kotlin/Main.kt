@file:JvmName("Main")

import logic.Algorithm
import logic.Game

fun main() {
    val algorithm = Algorithm.generateRandomAlgorithm()
    val game = Game(150, 40, 30, algorithm, 100, energyForOneFood = 20)

//    val initTime = System.currentTimeMillis()
//    for (i in 0 until 100) {
//        game.tick()
//    }

    while (true)
        game.tick()

//    val finishTime = System.currentTimeMillis()
//    println("Millis for 100 ticks: ${finishTime - initTime}")
}