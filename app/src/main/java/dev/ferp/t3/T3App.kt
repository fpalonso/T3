package dev.ferp.t3

import android.app.Application
import dev.ferp.t3.domain.model.GameBoard
import dev.ferp.t3.domain.model.GameEngine

class T3App : Application() {

    val board = GameBoard()
    val engine = GameEngine(board)
}