package gui

import java.awt.BorderLayout
import javax.swing.JInternalFrame
import javax.swing.JPanel

class GameWindow() : JInternalFrame ("Game field", true,true,true,true){
    val GV = GameVisualizer(this.width, this.height)

    init{
        val panel = JPanel(BorderLayout())
        panel.add(GV,BorderLayout.CENTER)
        contentPane.add(panel)
        pack()
    }
}