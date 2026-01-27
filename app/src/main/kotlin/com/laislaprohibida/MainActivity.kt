package com.laislaprohibida


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.laislaprohibida.view.GameBottomBar
import com.laislaprohibida.view.GameScreen
import com.laislaprohibida.view.GameTopBar
import com.laislaprohibida.view.HelpScreen
import com.laislaprohibida.view.RestartButton
import com.laislaprohibida.viewmodel.GameViewModel
import androidx.navigation.NavHostController
import com.laislaprohibida.model.GameModels
import com.laislaprohibida.ui.theme.GameTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GameTheme{
            val navController = rememberNavController()
            val vm: GameViewModel = viewModel()
            val state by vm.state.collectAsState()
            // Creamos el ViewModel
            Scaffold(
                topBar = { GameTopBar(navController) },
                bottomBar = { GameBottomBar(state) },
                floatingActionButton = { RestartButton(vm) }
            ) {
                AppNavigation(navController,vm,state)

            }

            }

        }
    }
}
@Composable
fun AppNavigation(nv: NavHostController, vm:GameViewModel,state: GameModels.GameState) {

    NavHost(nv, startDestination = "game") {
        composable("game") { GameScreen(state,vm) }
        composable("help") { HelpScreen(nv) }

    }
}
