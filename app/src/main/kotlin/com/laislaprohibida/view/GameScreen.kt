package com.laislaprohibida.view

import android.content.Intent

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.laislaprohibida.viewmodel.GameViewModel
import com.laislaprohibida.model.GameModels.GameState
import com.laislaprohibida.model.GameModels.Position
import com.laislaprohibida.model.GameModels.ActionMode
import com.laislaprohibida.model.GameModels.TileState
import com.laislaprohibida.model.GameModels.Tile
import com.laislaprohibida.R
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.draw.clip


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(state:GameState, vm: GameViewModel = viewModel()) {

        GameBody(state, vm)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(navController: NavController) {
    TopAppBar(
        title = { Text("Isla Prohibida") },
        actions = {
            IconButton(onClick = { navController.navigate("help") }) {
                Icon(Icons.Default.Info, contentDescription = "Ayuda")
            }
            IconButton(onClick = { navController.navigate("game") }) {
                Icon(Icons.Default.Home, contentDescription = "Configuración")
            }
            ShareGameButton()
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFF4CAF50),
            titleContentColor = Color.White
        )
    )
}


@Composable
fun GameBottomBar(state: GameState) {
    BottomAppBar(
        containerColor = Color(0xFF81C784)
    ) {
        Text(
            "Tesoros: ${state.treasuresCollected}   Agua: ${state.waterLevel}",
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun RestartButton(vm: GameViewModel) {
    FloatingActionButton(onClick = { vm.restart() }) {
        Icon(painterResource(R.drawable.rounded_360_24), contentDescription = null)
    }
}

@Composable
fun GameBody(state: GameState, vm: GameViewModel) {

    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(80.dp))

        // Selector de acción mover / salvar
        ActionSelector(state.actionMode, vm)

        Spacer(Modifier.height(8.dp))

        // Tablero de losetas
        state.board.forEachIndexed { r, row ->
            Row {
                row.forEachIndexed { c, tile ->
                    TileView(
                        tile = tile,
                        hasPlayer = state.playerPos == Position(r, c),
                        onClick = { vm.onTileSelected(Position(r, c)) }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        WaterLevelBar(currentLevel = state.waterLevel)

        // Mensaje de fin de juego
        if (state.gameOver) {
            Text(
                text = if (state.win) "¡Has ganado! 🎉" else "Has perdido 😢",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Red
            )
        }
    }
}

@Composable
fun ActionSelector(selected: ActionMode, vm: GameViewModel) {

    Row {
        Button(
            onClick = {vm.setAction(ActionMode.MOVE)},
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected == ActionMode.MOVE) Color(0xFF4CAF50) else Color.LightGray
            )
        ) {
            Text("Mover")
        }

        Button(
            onClick = {vm.setAction(ActionMode.SHORE_UP)},
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected == ActionMode.SHORE_UP) Color(0xFF4CAF50) else Color.LightGray
            )
        ) {
            Text("Salvar")
        }


    }
}




@Composable
fun TileView(tile: Tile, hasPlayer: Boolean, onClick: () -> Unit) {

    val color = when (tile.state) {
        TileState.NORMAL -> Color(0xFF81C784)
        TileState.FLOODED -> Color(0xFF64B5F6)
        TileState.SUNK -> Color.DarkGray
    }

    Box(
        modifier = Modifier
            .size(56.dp)
            .padding(2.dp)
            .background(color, RoundedCornerShape(8.dp))
            .clickable(enabled = tile.state != TileState.SUNK) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        when {
            hasPlayer -> Image(painter=painterResource(R.drawable.mono),contentDescription = null)
            tile.hasTreasure -> Image(painterResource(R.drawable.platano),contentDescription = null)
        }
    }
}


@Composable
fun HelpScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📝 Reglas del juego")
        Spacer(Modifier.height(8.dp))
        Text(
            "1. Recolecta los 4 tesoros.\n" +
                    "2. Evita que las losetas se hundan.\n" +
                    "3. Escapa antes de que el agua suba demasiado."
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("⬅ Volver")
        }
    }
}

@Composable
fun WaterLevelBar(currentLevel: Int, maxLevel: Int = 10) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text("Nivel de agua: $currentLevel / $maxLevel")
        Spacer(Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = currentLevel.toFloat() / maxLevel,
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp)),
            color = Color(0xFF64B5F6),
            trackColor = Color(0xFFB3E5FC)
        )
    }
}

@Composable
fun ShareGameButton() {
    val context = LocalContext.current

    IconButton(onClick = {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "¡Mira este juego increíble de Isla Prohibida! Descárgalo y juega ahora."
            )
            putExtra(Intent.EXTRA_SUBJECT, "Juego Isla Prohibida")
        }
        context.startActivity(
            Intent.createChooser(shareIntent, "Compartir juego vía")
        )
    }) {
        Icon(Icons.Default.Share, contentDescription = "Configuración")

    }
}


