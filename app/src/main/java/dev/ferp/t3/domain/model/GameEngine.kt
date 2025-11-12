package dev.ferp.t3.domain.model

class GameEngine(
    private val board: GameBoard,
    initialPlayer: Player = Player.CIRCLE
) {
    private var player = initialPlayer

    fun play(row: Int, column: Int) {
        val isValidMove = board.place(row, column, player)
        if (isValidMove) {
            player = player.opposite()
        }
    }
}