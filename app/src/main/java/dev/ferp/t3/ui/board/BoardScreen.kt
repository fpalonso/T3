package dev.ferp.t3.ui.board

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ferp.t3.domain.model.Cell
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
) {
    var isExpanded by remember { mutableStateOf(false) }
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

    Canvas(
        modifier
            .padding(horizontal = 64.dp)
            .aspectRatio(1f)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { (x, y) ->
                    val cellSize = size.width / viewModel.uiState.columns
                    val rowIndex = floor(y / cellSize.toDouble()).toInt()
                    val colIndex = floor(x / cellSize.toDouble()).toInt()

                    viewModel.onCellTap(
                        row = rowIndex,
                        column = colIndex
                    )
                }
            }
    ) {
        val board = Board(
            cellSize = (size.width / viewModel.uiState.columns).toInt(),
            cellPadding = 24.dp
        )
        val right = board.cellSize * viewModel.uiState.columns * sizePct
        val bottom = board.cellSize * viewModel.uiState.rows * sizePct

        // Draw vertical lines
        for (i in 1..<viewModel.uiState.columns) {
            val x = i * board.cellSize.toFloat()
            drawLine(
                start = Offset(x, 0f),
                end = Offset(x, bottom),
                color = color,
                strokeWidth = board.strokeWidth
            )
        }

        // Draw horizontal lines
        for (i in 1..<viewModel.uiState.rows) {
            val y = i * board.cellSize.toFloat()
            drawLine(
                start = Offset(0f, y),
                end = Offset(right, y),
                color = color,
                strokeWidth = board.strokeWidth
            )
        }

        // Draw X and O
        for (i in 0..<viewModel.uiState.cells.size) {
            if (viewModel.uiState.cells[i] == Cell.EMPTY) continue

            val row = floor(i / viewModel.uiState.columns.toDouble()).toInt()
            val col = i % viewModel.uiState.columns

            if (viewModel.uiState.cells[i] == Cell.CIRCLE) {
                drawO(board, row, col)
            } else {
                drawX(board, row, col)
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