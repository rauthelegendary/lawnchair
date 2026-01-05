package app.lawnchair.esde.data

sealed class BackgroundGameState : BackgroundState {
    data class Game(val data: GameMetadata?) : BackgroundGameState()
}
