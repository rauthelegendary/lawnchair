package app.lawnchair.esde.data

object EsdeSystemCache {

    private val systems = mutableMapOf<String, BackgroundSystemState>()

    fun get(system: String): BackgroundSystemState? =
        systems[system.lowercase()]

    fun add(system: String, state: BackgroundSystemState) {
        systems[system] = state
    }
}
