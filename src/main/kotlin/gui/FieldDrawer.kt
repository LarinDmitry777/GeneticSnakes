package gui

interface FieldDrawer {
    fun generateField(width: Int, height: Int)
    fun drawField(field: List<List<FieldElement>>)
    fun updatedElements(elements: Iterable<FieldElement>)
}