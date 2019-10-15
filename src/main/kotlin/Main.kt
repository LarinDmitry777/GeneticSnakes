@file:JvmName("Main")

import logic.Algorithm
import logic.Game
import gui.GUI
import java.util.*

fun main() {
    val algorithm = Algorithm.generateRandomAlgorithm()
    GUI.generateField(150, 50)
    val game = Game(150, 50, 30, algorithm, 250, energyForOneFood = 20)

//    val initTime = System.currentTimeMillis()
//    for (i in 0 until 100) {
//        game.tick()
//    }

    Timer().schedule(object : TimerTask() {
        override fun run(){
            game.tick()
        }
    }, 0, 50)


//    val finishTime = System.currentTimeMillis()
//    println("Millis for 100 ticks: ${finishTime - initTime}")
}