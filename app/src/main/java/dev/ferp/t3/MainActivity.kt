package dev.ferp.t3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dev.ferp.t3.ui.board.BoardScreen
import dev.ferp.t3.ui.board.BoardViewModel

class MainActivity : ComponentActivity() {

    private val boardViewModel: BoardViewModel by viewModels {
        BoardViewModel.Factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoardScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = boardViewModel
            )
        }
    }
}
