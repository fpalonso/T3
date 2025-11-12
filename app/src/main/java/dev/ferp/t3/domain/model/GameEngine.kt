package dev.ferp.t3.domain.model

class GameEngine(
    private val board: GameBoard,
    initialPlayer: Player = Player.CIRCLE
) {
    val isGameFinished: Boolean
        get() = board.isGameFinished

    private var player = initialPlayer

    fun play(row: Int, column: Int) {
        val isValidMove = board.place(row, column, player)
        if (isValidMove) {
            player = player.opposite()
        }
    }

    fun restartGame() = board.clear()
    fun checkWinner() = board.checkWinner()
}