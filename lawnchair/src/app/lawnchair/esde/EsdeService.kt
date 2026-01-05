package app.lawnchair.esde

import android.graphics.drawable.Drawable
import app.lawnchair.esde.data.BackgroundGameState
import app.lawnchair.esde.data.BackgroundSystemState
import app.lawnchair.esde.data.EsdeSystemCache
import app.lawnchair.esde.data.GameMetadata
import app.lawnchair.esde.data.GameMetadataCache
import app.lawnchair.esde.parser.EsdeGameXMLParser
import app.lawnchair.esde.parser.EsdeSystemXMLParser
import java.io.File



object EsdeService {

    fun processGamelists(){
        val gamelistDir = File(EsdePathResolver.getGamelistPath())
        if (!gamelistDir.exists() || !gamelistDir.isDirectory) return

        //go through each folder in the directory, if there's a gamelist.xml inside, parse each game found inside, uses folder name as system name
        gamelistDir.listFiles {file -> file.isDirectory}?.forEach { systemDir ->
            val systemName = systemDir.name

            val xml = File(systemDir, "gamelist.xml")
            if (!xml.exists()) return@forEach
            val gamelist : MutableMap<String, GameMetadata> = EsdeGameXMLParser.parseGameMetadata(xml)
            gamelist.forEach { (gameName, metadata) ->
                metadata.imagePath = EsdePathResolver.resolveImagePathMultipleSources(systemName, gameName)
                metadata.iconPath = EsdePathResolver.resolveImagePath( systemName, "/marquees/", gameName)
            }
            GameMetadataCache.put(systemName, gamelist)
        }
    }

    fun processSystems() {
        val systemDir = File(EsdePathResolver.getSystemXmlPath())
        if (!systemDir.exists() || !systemDir.isDirectory) return

        systemDir.listFiles { file -> file.isFile }?.forEach { systemXml ->
            val systemName = systemXml.nameWithoutExtension
            val imagePath = EsdePathResolver.resolveSystemImagePath(systemName)
            var image: Drawable? = null
            if (imagePath != null && imagePath.isNotEmpty()) {
                image = Drawable.createFromPath(imagePath)
            }
            val newBackgroundSystem = BackgroundSystemState.EsdeSystem(
                EsdeSystemXMLParser.parseEsdeSystemXml(systemXml), image
            )
            EsdeSystemCache.add(systemName, newBackgroundSystem)

        }
    }

    fun processGameSelected(title: String?, system: String?) : BackgroundGameState? {
        if(system != null && system.isNotEmpty() && title != null && title.isNotEmpty()) {
            val lastIndexDot: Int = title.lastIndexOf(".")
            val titleWithoutExtension = lastIndexDot.let { title.take(it) }

            //TODO: isn't there a thing in ESDE where you can display games outside the gameslist? That would fail to display anything
            val metadata: GameMetadata? = GameMetadataCache.get(system, titleWithoutExtension)
            if(metadata?.imagePath != null || metadata?.iconPath != null){
                return BackgroundGameState.Game(metadata)
            }
        }
        return null
    }

    fun getSystem(system: String?): BackgroundSystemState? {
        if(system != null && system.isNotEmpty()) {
            val cached = EsdeSystemCache.get(system)
            if (cached != null) {
                return cached
            }
        }
        return null
    }
}
