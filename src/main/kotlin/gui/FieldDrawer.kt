package gui

import logic.FieldCell

interface FieldDrawer {
    fun generateField(width: Int, height: Int)
    fun drawField(field: List<List<FieldElement>>)
    fun updatedElements(elements: Iterable<FieldCell>)
    fun updateDebugInfo(string: Iterable<String>)
    fun changeFoodCount(newFoodCount: Int)
}