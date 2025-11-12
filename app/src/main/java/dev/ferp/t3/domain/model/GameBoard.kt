package dev.ferp.t3.domain.model

class GameBoard(
    val rows: Int = DEFAULT_ROWS,
    val columns: Int = DEFAULT_COLUMNS
) {
    private val _cells = MutableList(rows * columns) { Cell.EMPTY }
    val cells: List<Int>
        get() = _cells.toList()

    val isGameFinished: Boolean
        get() = checkWinner() != null || isBoardComplete

    val isBoardComplete: Boolean
        get() = _cells.none { it == Cell.EMPTY }

    fun clear() = _cells.fill(Cell.EMPTY)

    fun isCellEmpty(row: Int, column: Int): Boolean {
        val cellIndex = cellIndexOf(row, column)
        return cells[cellIndex] == Cell.EMPTY
    }

    fun place(row: Int, column: Int, player: Player): Boolean {
        if (!isCellEmpty(row, column)) return false
        val cellIndex = cellIndexOf(row, column)
        _cells[cellIndex] = if (player == Player.CIRCLE) Cell.CIRCLE else Cell.CROSS
        return true
    }

    fun checkWinner(): Player? {
        var winner: Player?
        for (row in 0..<rows) {
            winner = checkRow(row)
            if (winner != null) return winner
        }
        for (col in 0..<columns) {
            winner = checkColumn(col)
            if (winner != null) return winner
        }
        winner = checkMainDiagonal()
        if (winner != null) return winner
        return checkAntiDiagonal()
    }

    private fun checkRow(row: Int): Player? {
        val startCell = cellIndexOf(row, 0)
        if (_cells[startCell] == Cell.EMPTY) return null
        for (col in 1..<columns) {
            val cellIndex = cellIndexOf(row, col)
            val previousCellIndex = cellIndexOf(row, col - 1)
            if (_cells[cellIndex] != _cells[previousCellIndex]) return null
        }
        return playerOf(_cells[startCell])
    }

    private fun checkColumn(column: Int): Player? {
        val startCell = cellIndexOf(0, column)
        if (_cells[startCell] == Cell.EMPTY) return null
        for (row in 1..<rows) {
            val cellIndex = cellIndexOf(row, column)
            val previousCellIndex = cellIndexOf(row - 1, column)
            if (_cells[cellIndex] != _cells[previousCellIndex]) return null
        }
        return playerOf(_cells[startCell])
    }

    private fun checkMainDiagonal(): Player? {
        val startCell = 0
        if (_cells[startCell] == Cell.EMPTY) return null
        for (row in 1..<rows) {
            val cellIndex = cellIndexOf(row, column = row)
            val previousCellIndex = cellIndexOf(row - 1, column = row - 1)
            if (_cells[cellIndex] != _cells[previousCellIndex]) return null
        }
        return playerOf(_cells[startCell])
    }

    private fun checkAntiDiagonal(): Player? {
        val startCell = columns - 1
        if (_cells[startCell] == Cell.EMPTY) return null
        for (row in 1..<rows) {
            val cellIndex = cellIndexOf(row, columns - 1 - row)
            val previousCellIndex = cellIndexOf(row - 1, columns - row)
            if (_cells[cellIndex] != _cells[previousCellIndex]) return null
        }
        return playerOf(_cells[startCell])
    }

    private fun playerOf(cellValue: Int) = when (cellValue) {
        Cell.CIRCLE -> Player.CIRCLE
        Cell.CROSS -> Player.CROSS
        else -> null
    }

    private fun cellIndexOf(row: Int, column: Int): Int {
        return row * columns + column
    }

    companion object {
        private const val DEFAULT_ROWS = 3
        private const val DEFAULT_COLUMNS = 3
    }
}