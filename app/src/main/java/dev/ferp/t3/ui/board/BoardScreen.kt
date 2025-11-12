package dev.ferp.t3.ui.board

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ferp.t3.domain.model.Cell
import dev.ferp.t3.domain.model.Player
import kotlin.math.floor

private data class Board(
    val cellSize: Int,
    val cellPadding: Dp,
    val strokeWidth: Float = Stroke.DefaultMiter
)

@Composable
fun BoardScreen(
    viewModel: BoardViewModel,
    modifier: Modifier = Modifier
) = BoardScreenContent(
    modifier = modifier,
    state = viewModel.uiState,
    onCellTap = viewModel::onCellTap,
    onRestartButtonClick = viewModel::restartGame
)

@Composable
private fun BoardScreenContent(
    state: BoardUiState,
    modifier: Modifier = Modifier,
    onCellTap: (Int, Int) -> Unit = { _, _ -> },
    onRestartButtonClick: () -> Unit = {},
    skipAnimation: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(skipAnimation) }
    LaunchedEffect(Unit) {
        isExpanded = true
    }
    val transition = updateTransition(targetState = isExpanded)
    val sizePct by transition.animateFloat(
        transitionSpec = { tween(1000) }
    ) { expanded ->
        if (expanded) 1f else 0f
    }
    val color by transition.animateColor(
        transitionSpec = { tween(1000) }
    ) { expanded ->
        if (expanded) Color.Black else Color.White
    }

    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(
            Modifier
                .padding(horizontal = 64.dp)
                .aspectRatio(1f)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures { (x, y) ->
                        val cellSize = size.width / state.columns
                        val rowIndex = floor(y / cellSize.toDouble()).toInt()
                        val colIndex = floor(x / cellSize.toDouble()).toInt()

                        onCellTap(rowIndex, colIndex)
                    }
                }
        ) {
            val board = Board(
                cellSize = (size.width / state.columns).toInt(),
                cellPadding = 24.dp
            )
            val right = board.cellSize * state.columns * sizePct
            val bottom = board.cellSize * state.rows * sizePct

            // Draw vertical lines
            for (i in 1..<state.columns) {
                val x = i * board.cellSize.toFloat()
                drawLine(
                    start = Offset(x, 0f),
                    end = Offset(x, bottom),
                    color = color,
                    strokeWidth = board.strokeWidth
                )
            }

            // Draw horizontal lines
            for (i in 1..<state.rows) {
                val y = i * board.cellSize.toFloat()
                drawLine(
                    start = Offset(0f, y),
                    end = Offset(right, y),
                    color = color,
                    strokeWidth = board.strokeWidth
                )
            }

            // Draw X and O
            for (i in 0..<state.cells.size) {
                if (state.cells[i] == Cell.EMPTY) continue

                val row = floor(i / state.columns.toDouble()).toInt()
                val col = i % state.columns

                if (state.cells[i] == Cell.CIRCLE) {
                    drawO(board, row, col)
                } else {
                    drawX(board, row, col)
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        if (state.isGameFinished) {
            val message = if (state.winner != null) {
                "${state.winner} wins!"
            } else {
                "It's a draw!"
            }
            Text(
                modifier = Modifier,
                text = message,
                style = MaterialTheme.typography.bodyLarge
            )
            Button(onRestartButtonClick) {
                Text("Restart")
            }
        }
    }
}

private fun DrawScope.drawO(
    board: Board,
    row: Int,
    col: Int,
) {
    val centerX = (col + 0.5f) * board.cellSize
    val centerY = (row + 0.5f) * board.cellSize
    val size = board.cellSize - board.cellPadding.toPx() * 2

    drawCircle(
        center = Offset(centerX, centerY),
        radius = size / 2,
        color = Color.Black,
        style = Stroke(
            width = board.strokeWidth
        )
    )
}

private fun DrawScope.drawX(
    board: Board,
    row: Int,
    col: Int,
) {
    val x = col * board.cellSize + board.cellPadding.toPx()
    val y = row * board.cellSize + board.cellPadding.toPx()
    val size = board.cellSize - board.cellPadding.toPx() * 2

    drawLine(
        start = Offset(x, y),
        end = Offset(x + size, y + size),
        color = Color.Black,
        strokeWidth = board.strokeWidth
    )
    drawLine(
        start = Offset(x + size, y),
        end = Offset(x, y + size),
        color = Color.Black,
        strokeWidth = board.strokeWidth
    )
}

@Preview
@Composable
private fun BoardScreenPreview() {
    BoardScreenContent(
        state = BoardUiState(
            rows = 3,
            columns = 3,
            winner = Player.CIRCLE
        ),
        skipAnimation = true
    )
}