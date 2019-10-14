package gui

import logic.FieldCell

object ConsoleGraphics : FieldDrawer {

    private const val WALL_CELL_CHAR = '_'
    private const val EMPTY_CELL_CHAR = ' '
    private const val SNAKE_BODY_CELL_CHAR = '#'
    private const val SNAKE_HEAD_CELL_CHAR = '%'
    private const val FOOD_CELL_CHAR = 'o'

    private fun FieldElement.toChar(): Char {
        return when (this) {
            FieldElement.EMPTY_CELL -> EMPTY_CELL_CHAR
            FieldElement.SNAKE_BODY -> SNAKE_BODY_CELL_CHAR
            FieldElement.SNAKE_HEAD -> SNAKE_HEAD_CELL_CHAR
            FieldElement.WALL -> WALL_CELL_CHAR
            FieldElement.FOOD -> FOOD_CELL_CHAR
        }
    }

    fun gameTick() {

    }

    private fun replaceChar(x: Int, y: Int, c: Char) {
        val escCode: Char = 0x1B.toChar()
        print(String.format("%c[%d;%df%c", escCode, y, x, c))
    }

    private fun cleanScreen() {
        ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
    }

    override fun generateField(width: Int, height: Int) {
        val strings = generateSequence { }
            .take(height + 2)
            .mapIndexed { index, _ ->
                if (index == 0 || index == height + 1)
                    WALL_CELL_CHAR.toString().repeat(width + 2)
                else
                    "$WALL_CELL_CHAR${" ".repeat(width)}$WALL_CELL_CHAR"
            }.joinToString("\n")
        println(strings)
    }

    override fun drawField(field: List<List<FieldElement>>) {
        val horizontalWall = WALL_CELL_CHAR.toString().repeat(field[0].size + 2)
        val strings = field.map { s ->
            s.map{it.toChar()}
                .joinToString("")
        }.joinToString("\n")
        cleanScreen()
        println(strings)
    }

    override fun updatedElements(elements: Iterable<FieldCell>) {
        elements.forEach{
            replaceChar(it.position.x + 2, it.position.y + 2, it.value.toChar())
        }
        replaceChar(0, 30, ' ')
    }

}