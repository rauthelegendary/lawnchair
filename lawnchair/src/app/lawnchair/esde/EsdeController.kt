package app.lawnchair.esde

import app.lawnchair.esde.data.BackgroundGameState
import app.lawnchair.esde.data.BackgroundState
import app.lawnchair.esde.data.BackgroundSystemState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

object EsdeController {

    private val _gameState =
        MutableStateFlow<BackgroundGameState?>(null)
    private val _systemState =
        MutableStateFlow<BackgroundSystemState?>(null)
    private val _gamePickedLast =
        MutableStateFlow(false)

    val state: StateFlow<BackgroundState?> =
        combine(
            _gameState,
            _systemState,
            _gamePickedLast
        ) { game, system, gamePickedLast ->
            if (gamePickedLast) game else system
        }
            .stateIn(
                scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
                started = SharingStarted.Eagerly,
                initialValue = null
            )

    fun setGameState(state: BackgroundGameState?) {
        _gameState.value = state
        _gamePickedLast.value = true
    }

    fun setSystemState(state: BackgroundSystemState?) {
        _systemState.value = state
        _gamePickedLast.value = false
    }
}
