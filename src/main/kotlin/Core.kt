import gui.GUI
import logic.Algorithm
import logic.Game
import java.util.*

object Core {
    fun generateGame(w: Int, h: Int){
        val algorithm = Algorithm.generateRandomAlgorithm()
        GUI.generateField(w, h)
        val game = Game(w, h, 30, algorithm, 250, energyForOneFood = 20)
        Timer().schedule(object : TimerTask() {
            override fun run(){
                game.tick()
                GUI.drawField(game.field)
            }
        }, 0, 50)
    }
}