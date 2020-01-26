@file:JvmName("Main")

import logic.Algorithm
import logic.Game
import gui.GUI
import java.io.FileInputStream
import java.util.*
import java.io.ObjectInputStream


fun main() {
    // Загрузка алгоритма
//    val algorithmGeneration = 139
//    val fileInputStream = FileInputStream("algorithms/algorithm$algorithmGeneration.alg")
//    val objectInputStream = ObjectInputStream(fileInputStream)
//    val algorithm = objectInputStream.readObject() as Algorithm

    //Запуск игры
    Core.generateGame(450, 150, 270, 2070, algorithm = null, isNeedDebugInfo = true)

    //Загрузка игры
//    Core.loadGame(1087, true)
}