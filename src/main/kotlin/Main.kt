@file:JvmName("Main")

import logic.Algorithm
import logic.Game

fun main() {
    val algorithm = Algorithm.generateRandomAlgorithm()
    val game = Game(150, 40, 30, algorithm, 450, energyForOneFood = 20)
    while(true) {
        game.tick()
        Thread.sleep(100)
    }
}