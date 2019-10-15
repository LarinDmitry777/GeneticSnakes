import gui.GUI
import logic.Algorithm
import logic.Game
import java.util.*

object Core {
    fun generateGame(width: Int, height: Int, initSnakesCount: Int, maxFoodCount: Int, energyForOneFood: Int = 20){
        val algorithm = Algorithm.generateRandomAlgorithm()
        GUI.generateField(width, height)
        val game = Game(width, height, initSnakesCount, algorithm, maxFoodCount, energyForOneFood = energyForOneFood)
        Timer().schedule(object : TimerTask() {
            override fun run(){
                game.tick()
                GUI.drawField(game.field)
            }
        }, 0, 50)
    }
}