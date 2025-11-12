package dev.ferp.t3.ui.board

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import dev.ferp.t3.T3App
import dev.ferp.t3.domain.model.GameBoard
import dev.ferp.t3.domain.model.GameEngine
import kotlin.reflect.KClass

data class BoardUiState(
    val rows: Int = 0,
    val columns: Int = 0,
    val cells: List<Int> = emptyList()
)

class BoardViewModel(
    private val board: GameBoard,
    private val engine: GameEngine
) : ViewModel() {

    var uiState by mutableStateOf(
        BoardUiState(board.rows, board.columns, board.cells)
    )
        private set

    fun onCellTap(row: Int, column: Int) {
        if (!board.isCellEmpty(row, column)) return
        engine.play(row, column)
        uiState = uiState.copy(cells = board.cells)
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: KClass<T>,
                extras: CreationExtras
            ): T {
                val app = extras[APPLICATION_KEY] as T3App
                return BoardViewModel(app.board, app.engine) as T
            }
        }
    }
}