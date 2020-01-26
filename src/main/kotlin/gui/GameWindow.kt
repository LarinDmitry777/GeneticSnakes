package gui

import java.awt.BorderLayout
import javax.swing.JInternalFrame
import javax.swing.JPanel

class GameWindow(width: Int, height: Int) : JInternalFrame ("Game field", true,true,true,true){
    val GV : GameVisualizer = GameVisualizer(width, height)

    init{
        val panel = JPanel(BorderLayout())
        panel.add(GV,BorderLayout.CENTER)
        contentPane.add(panel)
        pack()
    }
}