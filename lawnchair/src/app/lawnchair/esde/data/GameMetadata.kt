package app.lawnchair.esde.data

data class GameMetadata(
    val filename: String,
    val name: String?,
    val description: String?,
    val releaseDate: String?,
    val developer: String?,
    val publisher: String?,
    val players: String?,
    var imagePath: String?,
    var iconPath: String?
)
