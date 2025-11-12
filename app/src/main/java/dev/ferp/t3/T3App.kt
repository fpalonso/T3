package dev.ferp.t3

import android.app.Application
import dev.ferp.t3.domain.model.GameBoard

class T3App : Application() {

    val board = GameBoard()
}