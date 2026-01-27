package com.laislaprohibida.logic
import com.laislaprohibida.model.GameModels.*
import kotlin.math.abs

class GameLogic {

// ---------- INITIALIZATION ----------

    /** Crea el estado inicial del juego con tablero 5x5 y 4 tesoros fijos */
    fun initialGameState(): GameState =
        GameState(
            board = createInitialBoard(),
            playerPos = Position(2, 2)
        )

    private fun createInitialBoard(): List<List<Tile>> {
        val treasurePositions = listOf(
            Position(0, 0), Position(0, 4),
            Position(4, 0), Position(4, 4)
        )

        var id = 0
        return List(5) { r ->
            List(5) { c ->
                Tile(
                    id = id++,
                    hasTreasure = treasurePositions.any { it.row == r && it.col == c }
                )
            }
        }
    }

// ---------- HELPER FUNCTIONS ----------

    /** Verifica si dos posiciones son adyacentes (arriba, abajo, izquierda, derecha) */
    fun isAdjacent(a: Position, b: Position): Boolean =
        (abs(a.row - b.row) + abs(a.col - b.col)) == 1

// ---------- TURN LOGIC ----------

    /** Mueve al jugador a la posición indicada y recoge tesoro si hay */
    fun applyMove(state: GameState, pos: Position): GameState {
        if (state.gameOver) return state

        val tile = state.board[pos.row][pos.col]
        if (tile.state == TileState.SUNK) return state
        if (!isAdjacent(state.playerPos, pos)) return state

        // Recoge tesoro si está en la loseta
        var treasures = state.treasuresCollected
        val newBoard = state.board.mapIndexed { r, row ->
            row.mapIndexed { c, t ->
                if (r == pos.row && c == pos.col && t.hasTreasure && t.state != TileState.SUNK)
                    t.copy(hasTreasure = false)
                else t
            }
        }
        if (tile.hasTreasure) treasures++

        // Actualiza estado y aplica efectos post-turno (hundimiento)
        return afterPlayerAction(
            state.copy(
                board = newBoard,
                playerPos = pos,
                treasuresCollected = treasures,
                moves = state.moves + 1
            )
        )
    }

    /** Salva una loseta inundada (FLOODED → NORMAL) */
    fun applyShoreUp(state: GameState, pos: Position): GameState {
        if (state.gameOver) return state

        val tile = state.board[pos.row][pos.col]
        if (tile.state != TileState.FLOODED) return state

        val newBoard = state.board.mapIndexed { r, row ->
            row.mapIndexed { c, t ->
                if (r == pos.row && c == pos.col)
                    t.copy(state = TileState.NORMAL)
                else t
            }
        }

        return afterPlayerAction(
            state.copy(
                board = newBoard,
                moves = state.moves + 1
            )
        )
    }

// ---------- POST-TURN EFFECTS ----------

    /** Aplica efectos que ocurren después de cada acción del jugador */
    private fun afterPlayerAction(state: GameState): GameState {
        var newState = state

        // Cada 4 movimientos, sube el nivel de agua
        if (state.moves % 4 == 0) {
            newState = floodTiles(newState, newState.waterLevel + 1)
        }

        return checkEnd(newState)
    }

    /** Inunda losetas aleatorias según nivel de agua */
    private fun floodTiles(state: GameState, newWater: Int): GameState {
        val flat = state.board.flatten()
        val candidates = flat.filter { it.state != TileState.SUNK }.shuffled()
        val toFlood = candidates.take(newWater)

        val newBoard = state.board.map { row ->
            row.map { tile ->
                if (toFlood.any { it.id == tile.id }) {
                    when (tile.state) {
                        TileState.NORMAL -> tile.copy(state = TileState.FLOODED)
                        TileState.FLOODED -> tile.copy(state = TileState.SUNK)
                        TileState.SUNK -> tile
                    }
                } else tile
            }
        }

        return state.copy(board = newBoard, waterLevel = newWater)
    }

    /** Comprueba si el juego terminó */
    private fun checkEnd(state: GameState): GameState {
        val playerTile = state.board[state.playerPos.row][state.playerPos.col]

        // Pierde si la loseta del jugador se hunde
        if (playerTile.state == TileState.SUNK)
            return state.copy(gameOver = true, win = false)

        // Gana si recolectó los 4 tesoros
        if (state.treasuresCollected >= 4)
            return state.copy(gameOver = true, win = true)

        return state
    }

}