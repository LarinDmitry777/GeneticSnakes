package gui

import jdk.jfr.Event
import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;
import java.awt.*;
import java.util.Timer
import java.util.TimerTask
import javax.swing.JComponent

class GameVisualizer (width: Int, height: Int): JPanel(), FieldDrawer{
   // val timer = Timer("e generator", true)


    init{
        onRedrawEvent()

        generateField(10, 10)
    }
    override fun generateField(width: Int, height: Int) {
        EventQueue.invokeLater(drawField(width, height))
    }

    fun drawField(width: Int, height: Int) : Runnable{
        //onRedrawEvent()
        val bounds = 20;
        val offsetW : Int = (this.width - bounds) / width

        graphics.color = Color.BLACK

        for (i in 0..width){
            graphics.drawLine(bounds, bounds + i * offsetW, this.width - bounds, bounds + i * offsetW)
        }
    }

    override fun paint(g: Graphics?) {
//        val width = 10
//        val bounds = 20;
//        val offsetW : Int = (this.width - bounds) / width
//        g!!.color = Color.BLACK
//
//        for (i in 0..width){
//            g.drawLine(bounds, bounds + i * offsetW, this.width - bounds, bounds + i * offsetW)
//        }

    }

    override fun drawField(field: List<List<FieldElement>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updatedElements(elements: Iterable<FieldElement>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    fun onRedrawEvent() = EventQueue.invokeLater(this::repaint)
}