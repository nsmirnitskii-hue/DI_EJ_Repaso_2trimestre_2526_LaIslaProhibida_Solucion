package com.laislaprohibida.model

class GameModels {
    enum class TileState { NORMAL, FLOODED, SUNK }

    data class Tile(
        val id: Int,
        val hasTreasure: Boolean = false,
        val state: TileState = TileState.NORMAL
    )

    data class Position(val row: Int, val col: Int)

    enum class ActionMode { MOVE, SHORE_UP }

    data class GameState(
        val board: List<List<Tile>>,
        val playerPos: Position,
        val moves: Int = 0,
        val waterLevel: Int = 1,
        val treasuresCollected: Int = 0,
        val actionMode: ActionMode = ActionMode.MOVE,
        val gameOver: Boolean = false,
        val win: Boolean = false
    )
}