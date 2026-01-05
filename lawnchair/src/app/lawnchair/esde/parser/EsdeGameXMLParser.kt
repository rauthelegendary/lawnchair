package app.lawnchair.esde.parser

import android.util.Xml
import app.lawnchair.esde.data.GameMetadata
import org.xmlpull.v1.XmlPullParser
import java.io.File
import java.io.FileInputStream

object EsdeGameXMLParser {

    fun parseGameMetadata(file: File): MutableMap<String, GameMetadata> {
        val parser = Xml.newPullParser()
        parser.setInput(FileInputStream(file), null)

        var eventType = parser.eventType

        var filename: String? = null
        var name: String? = null
        var description: String? = null
        var releaseDate: String? = null
        var developer: String? = null
        var publisher: String? = null
        var players: String? = null
        val gameMap: MutableMap<String, GameMetadata> = mutableMapOf()

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "game" -> {
                            filename = null
                            name = null
                            description = null
                            releaseDate = null
                            developer = null
                            publisher = null
                            players = null
                        }

                        "path" -> filename = parser.nextText()
                        "name" -> name = parser.nextText()
                        "desc" -> description = parser.nextText()
                        "releasedate" -> releaseDate = parser.nextText()
                        "developer" -> developer = parser.nextText()
                        "publisher" -> publisher = parser.nextText()
                        "players" -> players = parser.nextText()
                    }
                }

                XmlPullParser.END_TAG -> {
                    if (parser.name == "game" && filename != null) {
                        val normalizedFilename = normalizeGameName(filename)
                        gameMap[normalizedFilename] = GameMetadata(
                            filename = normalizedFilename,
                            name = name,
                            description = description,
                            releaseDate = releaseDate,
                            developer = developer,
                            publisher = publisher,
                            players = players,
                            imagePath = "",
                            iconPath = ""
                        )
                    }
                }
            }
            eventType = parser.next()
        }
        return gameMap
    }

    fun normalizeGameName(input: String?): String {
        return File(input).nameWithoutExtension
    }
}
