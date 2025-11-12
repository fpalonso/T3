package dev.ferp.t3.domain.model

class GameBoard(
    val rows: Int = DEFAULT_ROWS,
    val columns: Int = DEFAULT_COLUMNS
) {
    private val _cells = MutableList(rows * columns) { 0 }
    val cells: List<Int>
        get() = _cells.toList()

    fun isCellEmpty(row: Int, column: Int): Boolean {
        val cellIndex = cellIndexOf(row, column)
        return cells[cellIndex] == 0
    }

    fun place(row: Int, column: Int, player: Player) {
        val cellIndex = cellIndexOf(row, column)
        _cells[cellIndex] = if (player == Player.CIRCLE) Cell.CIRCLE else Cell.CROSS
    }

    private fun cellIndexOf(row: Int, column: Int): Int {
        return row * columns + column
    }

    companion object {
        private const val DEFAULT_ROWS = 3
        private const val DEFAULT_COLUMNS = 3
    }
}