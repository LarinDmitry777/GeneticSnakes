package gui
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import kotlin.system.exitProcess
import javax.swing.*;


object GUI : JFrame("sss"), FieldDrawer{
    override fun generateField(width: Int, height: Int) {
        gameWindow = GameWindow(width, height)
        val screenSize = Toolkit.getDefaultToolkit().getScreenSize()
        this.setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2)
        contentPane = desktopPane
        gameWindow!!.size = Dimension(400, 400)
        addWindow(gameWindow!!)
        addWindowListener(object : WindowListener{
            override fun windowDeiconified(p0: WindowEvent?) {
            }

            override fun windowClosed(p0: WindowEvent?) {
                JOptionPane.showMessageDialog(null, "Ну все ГГ")
            }

            override fun windowActivated(p0: WindowEvent?) {
            }

            override fun windowDeactivated(p0: WindowEvent?) {
            }

            override fun windowOpened(p0: WindowEvent?) {
                JOptionPane.showMessageDialog(null, "Пошевелим змейками?")
            }

            override fun windowIconified(p0: WindowEvent?) {
            }

            override fun windowClosing(p0: WindowEvent?) {
                close()
            }

        })
        this.menuBar = createMenuBar()

        desktopPane.background = Color(186,219,173)


        this.pack()
        this.isVisible = true
        this.extendedState = Frame.MAXIMIZED_BOTH
    }

    override fun drawField(field: List<List<FieldElement>>) {
        gameWindow!!.GV.drawField(field)
    }

    override fun updatedElements(elements: Iterable<FieldElement>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateDebugInfo(string: Iterable<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeFoodCount(newFoodCount: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val desktopPane = JDesktopPane()

    var gameWindow : GameWindow? = null
    private val inset = 50


    private fun createMenuBar () : MenuBar {
        val bar = MenuBar()
        val menu = Menu("Штуки дрюки")
        menu.add(MenuItem("Exit"))
        bar.add(menu)
        //Todo
        return bar
    }

    private fun addWindow(frame : JInternalFrame) {
        desktopPane.add(frame)
        frame.isVisible = true
    }

    fun close() = if (JOptionPane.showConfirmDialog(null, "Exit?") == JOptionPane.YES_OPTION )
        exitProcess(0) else null

}