package gui
import javafx.css.Size
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import kotlin.system.exitProcess
import javax.swing.*;


class GUI() : JFrame("sss") {

    val desktopPane = JDesktopPane()

    val gameWindow = GameWindow()
    private val inset = 50



    init {
        val screenSize = Toolkit.getDefaultToolkit().getScreenSize()
        this.setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2)
        contentPane = desktopPane
        gameWindow.size = Dimension(400, 400)
        addWindow(gameWindow)
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