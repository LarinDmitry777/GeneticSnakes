import gui.GUI
import logic.Algorithm
import logic.Game
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.util.*

object Core {
    fun printDebugData(game: Game) {
        val debugData = game.getDebugData()
            .toList()
            .sortedBy { it.first }
            .joinToString("\n") { pair -> "${pair.first} = ${pair.second}" }
        ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
        println(debugData)
    }

    fun generateGame(
        width: Int,
        height: Int,
        initSnakesCount: Int,
        maxFoodCount: Int,
        energyForOneFood: Int = 20,
        algorithm: Algorithm? = null,
        isNeedDebugInfo: Boolean = true
    ) {
        val game = Game(width, height, initSnakesCount, algorithm, maxFoodCount, energyForOneFood = energyForOneFood)
        run(game, isNeedDebugInfo)
    }

    fun loadGame(generation: Int, isNeedDebugInfo: Boolean = true) {
        val fileInputStream = FileInputStream("games/game$generation.save")
        val objectInputStream = ObjectInputStream(fileInputStream)
        val game = objectInputStream.readObject() as Game
        fileInputStream.close()
        run(game, isNeedDebugInfo)
    }

    private fun run(game: Game, isNeedDebugInfo: Boolean) {
        GUI.generateField(game.fieldWidth, game.fieldHeight)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                game.tick()
                if (isNeedDebugInfo && game.ticksCount % 1000 == 0)
                    printDebugData(game)
                GUI.drawField(game.field)
            }
        }, 0, 1)
    }
}