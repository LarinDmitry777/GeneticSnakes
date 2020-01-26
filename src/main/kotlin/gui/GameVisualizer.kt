package gui

import java.awt.*;
import java.lang.annotation.ElementType

import javax.swing.JPanel;
import java.util.*

class GameVisualizer(w_cell: Int, h_cell: Int) : JPanel() {

    var h_cell = 0
    var w_cell = 0
    val bounds = 20;
    var _field: List<List<FieldElement>>? = null


    init {
        this.h_cell = h_cell
        this.w_cell = w_cell
    }


    fun drawField(g: Graphics?) {

        val offsetH: Int = (this.height - 2 * bounds) / h_cell
        val offsetW: Int = (this.width - 2 * bounds) / w_cell

        for (i in 0..h_cell)

            g!!.drawLine(bounds, bounds + i * offsetH, this.width - bounds, bounds + i * offsetH)

        for (j in 0..w_cell)
            g!!.drawLine(bounds + j * offsetW, bounds, bounds + j * offsetW, this.height - bounds)

        var x = 0
        var y = 0
        if (_field === null) return
        for (line in _field!!) {
            for (e in line) {
                when (e) {
                    FieldElement.FOOD -> {
                        g!!.color = Color.BLUE
                        g.fillRect(
                            bounds + x * (offsetW),
                            bounds + y * (offsetH), offsetW, offsetH
                        )
                    }
                    FieldElement.SNAKE_BODY -> {
                        g!!.color = Color.CYAN
                        g.fillRect(
                            bounds + x * (offsetW),
                            bounds + y * (offsetH), offsetW, offsetH
                        )
                    }
                    FieldElement.SNAKE_HEAD -> {
                        g!!.color = Color.RED
                        g.fillRect(
                            bounds + x * (offsetW),
                            bounds + y * (offsetH), offsetW, offsetH
                        )
                    }

                    FieldElement.WALL -> {
                        g!!.color = Color.BLACK
                        g.fillRect(
                            bounds + x * (offsetW),
                            bounds + y * (offsetH), offsetW, offsetH
                        )
                    }

//                    FieldElement.EMPTY_CELL -> {
//                        g!!.color = Color.WHITE
//                        g.fillRect(
//                            bounds + x * (offsetW),
//                            bounds + y * (offsetH), offsetW, offsetH
//                        )
//                    }
                }
                x++

            }
            y++
            x = 0
        }


    }

    override fun paint(g: Graphics?) {
        super.paint(g)
        drawField(g)

    }

    fun drawField(field: List<List<FieldElement>>) {
        _field = field
        EventQueue.invokeLater(this::repaint)
    }
}