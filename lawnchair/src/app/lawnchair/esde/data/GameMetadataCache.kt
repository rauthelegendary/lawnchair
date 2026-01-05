package app.lawnchair.esde.data

object GameMetadataCache {
    private val gameMetadata =
        mutableMapOf<String, MutableMap<String, GameMetadata>>()

    fun put(system: String, metadata: MutableMap<String, GameMetadata>) {
        gameMetadata[system] = metadata
    }

    fun get(system: String, game: String): GameMetadata? =
        gameMetadata[system]?.get(game)

    fun clear() {
        gameMetadata.clear()
    }
}
