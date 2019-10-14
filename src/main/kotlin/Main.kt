import logic.Algorithm
import gui.GUI
import gui.FieldElement
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.util.*


fun main() {
    val alg = Algorithm()
    val objectOutputStream = ObjectOutputStream(
        FileOutputStream("alg.out")!!
    )
    val gui = GUI();
    objectOutputStream.writeObject(alg)


    //Это типо связка, делаешь свои изменения а потом даешь мне то что изменилось
    Timer().schedule(object : TimerTask() {
        override fun run(){
            //TODO делаешь ченить
            //gui.gameWindow.GV.onRedrawEvent()
            //gui.gameWindow.GV.updatedElements(listOf())// и пихаешь
            // а пихать можно только енамы что в гуе синк эбоут ит
            gui.gameWindow.GV.drawField(listOf(listOf(FieldElement.WALL, FieldElement.SNAKE_BODY), listOf(FieldElement.FOOD, FieldElement.SNAKE_HEAD)))

        }
    }, 0, 50)



    objectOutputStream.close()
}