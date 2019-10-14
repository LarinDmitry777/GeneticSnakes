package gui

import java.awt.*;
import java.lang.annotation.ElementType

import javax.swing.JPanel;
import java.util.*

class GameVisualizer (width: Int, height: Int): JPanel(), FieldDrawer{

    var h_cell = 0
    var w_cell = 0
    val bounds = 20;
    var _field: List<List<FieldElement>>? = null


    init{
        generateField(10, 10)
    }

    override fun generateField(width: Int, height: Int) {
        h_cell = width;
        w_cell = height

    }

    fun drawField(g: Graphics?){

        val offsetH : Int = (this.height - 2 * bounds) / h_cell
        val offsetW : Int = (this.width - 2 * bounds) / w_cell

        for (i in 0..h_cell)
            g!!.drawLine(bounds, bounds + i * offsetH, this.width - bounds, bounds + i * offsetH)

        for (j in 0..w_cell)
            g!!.drawLine(bounds + j * offsetW, bounds, bounds + j * offsetW, this.height - bounds)

        var x = 0
        var y = 0
        if (_field === null) return
        for (line in _field!!) {
            for (e in line) {
                when (e){
                    FieldElement.FOOD -> {
                        g!!.color = Color.GREEN
                        g.fillOval(bounds + x * (offsetW ),
                        bounds + y * (offsetH), offsetW , offsetH)}
                    FieldElement.SNAKE_BODY -> {
                        g!!.color = Color.BLUE
                        g.fillOval(bounds + x * (offsetW ),
                            bounds + y * (offsetH), offsetW , offsetH)}
                    FieldElement.SNAKE_HEAD -> {
                        g!!.color = Color.RED
                        g.fillOval(bounds + x * (offsetW ),
                            bounds + y * (offsetH), offsetW , offsetH)}

                    FieldElement.WALL -> {
                        g!!.color = Color.BLACK
                        g.fillRect(bounds + x * (offsetW ),
                            bounds + y * (offsetH), offsetW , offsetH)
                    }
                }
                x++

            }
            y++
            x = 0
        }


    }

    override fun updateDebugInfo(string: Iterable<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeFoodCount(newFoodCount: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun paint(g: Graphics?) {
        drawField(g)

    }

    override fun drawField(field: List<List<FieldElement>>) {
        _field = field
        EventQueue.invokeLater(this::repaint)
    }

    override fun updatedElements(elements: Iterable<FieldElement>) {

    }



}