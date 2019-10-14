package gui

import java.awt.*;

import javax.swing.JPanel;
import java.util.*

class GameVisualizer (width: Int, height: Int): JPanel(), FieldDrawer{
    val queue: Queue<(Graphics?) -> Unit> = ArrayDeque<(Graphics?) -> Unit>()


   // val timer = Timer("e generator", true)
    init{
        repaint()

        generateField(10, 10)
    }

    override fun generateField(width: Int, height: Int) {
        drawField(width, height)
    }

    fun drawField(width: Int, height: Int){
        //onRedrawEvent()
        val bounds = 20;
        val offsetH : Int = (this.height - 2 * bounds) / height
        val offsetW : Int = (this.width - 2 * bounds) / width


        for (i in 0..height)
            //graphics.drawLine(bounds, bounds + i * offsetH, this.width - bounds, bounds + i * offsetH)
            queue.add { drawLine(bounds, bounds + i * offsetH, this.width - bounds, bounds + i * offsetH) }

        for (j in 0..width)
//            graphics.drawLine(bounds + j * offsetW, bounds, bounds + j * offsetW, this.height - bounds)
              queue.add { drawLine(bounds + j * offsetW, bounds, bounds + j * offsetW, this.height - bounds) }
        repaint()
    }

    val drawLine : Function<Int, Int,Int, Int, Unit> = //{(x1: Int, x2: Int, x3: Int, x4: Int) : int-> x1 + x2}
//    fun drawLine(x1: Int, x2: Int, x3: Int, x4: Int) :(Graphics) -> Unit {g : Graphics? -> {
//        g!!.drawLine(x1,x2,x3,x4)
//        println("Line - $x1 $x2 $x3 $x4 ")
//    }}
//    fun dr(g: Graphics)
    //fun drawLine(x1: Int, x2: Int, x3: Int, x4: Int) = {g : Graphics ->  g.drawLine(x1,x2,x3,x4)}

    override fun paint(g: Graphics?) {
//        val width = 10
//        val bounds = 20;
//        val offsetW : Int = (this.width - bounds) / width
//        g!!.color = Color.BLACK
//
//        for (i in 0..width){
//            g.drawLine(bounds, bounds + i * offsetW, this.width - bounds, bounds + i * offsetW)
//        }
//        while (!queue.isEmpty()) {
//            queue.poll()(g)
//            println(queue.count())
//        }

        for (e in queue) {
            e(g)
            println(e)
        }

    }

    override fun drawField(field: List<List<FieldElement>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updatedElements(elements: Iterable<FieldElement>) {

        repaint()

    }


    fun onRedrawEvent() = EventQueue.invokeLater(this::repaint)
}