package com.laislaprohibida.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.laislaprohibida.logic.GameLogic
import com.laislaprohibida.model.GameModels.*


class GameViewModel : ViewModel() {

    val gl = GameLogic()

    private val _state = MutableStateFlow(gl.initialGameState())
    val state: StateFlow<GameState> = _state

    fun onTileSelected(pos: Position) {
        val current = _state.value

        val newState = when (current.actionMode) {
            ActionMode.MOVE -> gl.applyMove(current, pos)
            ActionMode.SHORE_UP -> gl.applyShoreUp(current, pos)
        }

        _state.value = newState
    }

    fun setAction(mode: ActionMode) {
        _state.value = _state.value.copy(actionMode = mode)
    }

    fun restart() {
        _state.value = gl.initialGameState()
    }
}
