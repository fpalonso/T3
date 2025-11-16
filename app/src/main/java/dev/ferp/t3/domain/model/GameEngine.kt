package dev.ferp.t3.domain.model

class GameEngine(
    private val board: GameBoard,
    initialPlayer: Player = Player.CIRCLE
) {
    val isGameFinished: Boolean
        get() = board.isGameFinished

    private var player = initialPlayer

    fun play(row: Int, column: Int) {
        val cellIndex = board.cellIndexOf(row, column)
        play(cellIndex)
    }

    private fun play(cellIndex: Int) {
        val isValidMove = board.place(cellIndex, player)
        if (isValidMove && !isGameFinished) {
            player = player.opposite()
            if (player == Player.CROSS) {
                aiMove()
            }
        }
    }

    private fun aiMove() {
        if (isGameFinished) {
            throw IllegalStateException("Game is finished already")
        }
        var bestCellIndex = -1
        var maxScore = Int.MIN_VALUE
        for (i in 0..<board.cells.size) {
            if (!board.isCellEmpty(i)) continue
            board.place(i, Player.CROSS)
            val score = minimax(isAiTurn = false)
            board.clearCell(i)
            if (score > maxScore) {
                maxScore = score
                bestCellIndex = i
            }
        }
        play(bestCellIndex)
    }

    private fun minimax(isAiTurn: Boolean): Int {
        if (isGameFinished) {
            return when (checkWinner()) {
                Player.CIRCLE -> -1
                Player.CROSS -> +1
                else -> 0
            }
        }

        if (isAiTurn) {
            var maxScore = Int.MIN_VALUE
            for (i in 0..<board.cells.size) {
                if (!board.isCellEmpty(i)) continue
                board.place(i, Player.CROSS)
                val score = minimax(isAiTurn = false)
                board.clearCell(i)
                if (score > maxScore) {
                    maxScore = score
                }
            }
            return maxScore
        } else {
            var minScore = Int.MAX_VALUE
            for (i in 0..<board.cells.size) {
                if (!board.isCellEmpty(i)) continue
                board.place(i, Player.CIRCLE)
                val score = minimax(isAiTurn = true)
                board.clearCell(i)
                if (score < minScore) {
                    minScore = score
                }
            }
            return minScore
        }
    }

    fun restartGame() = board.clear()
    fun checkWinner() = board.checkWinner()
}