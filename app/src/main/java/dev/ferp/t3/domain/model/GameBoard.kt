package dev.ferp.t3.domain.model

/**
 * Represents the tic-tac-toe board.
 *
 * @param boardSize Number of rows or columns in the board.
 */
class GameBoard(
    private val boardSize: Int = BOARD_SIZE
) {
    val rows = boardSize
    val columns = boardSize

    private val _cells = MutableList(rows * columns) { Cell.EMPTY }
    val cells: List<Int>
        get() = _cells.toList()

    internal val isGameFinished: Boolean
        get() = hasWinner || isBoardComplete

    private val hasWinner: Boolean
        get() = checkWinner() != null

    private val isBoardComplete: Boolean
        get() = _cells.none { it == Cell.EMPTY }

    internal fun clear() = _cells.fill(Cell.EMPTY)

    internal fun isCellEmpty(index: Int): Boolean = _cells[index] == Cell.EMPTY

    internal fun place(cellIndex: Int, player: Player): Boolean {
        if (!isCellEmpty(cellIndex) || isGameFinished) return false
        _cells[cellIndex] = if (player == Player.CIRCLE) Cell.CIRCLE else Cell.CROSS
        return true
    }

    internal fun clearCell(index: Int) {
        _cells[index] = Cell.EMPTY
    }

    internal fun checkWinner(): Player? {
        var winner: Player?
        for (row in 0..<boardSize) {
            winner = checkRow(row)
            if (winner != null) return winner
        }
        for (col in 0..<boardSize) {
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

    internal fun cellIndexOf(row: Int, column: Int): Int {
        return row * columns + column
    }

    companion object {
        /**
         * Number of rows and cols of the board.
         * Increasing this value will make the Minimax algorithm take too long.
         */
        private const val BOARD_SIZE = 3
    }
}