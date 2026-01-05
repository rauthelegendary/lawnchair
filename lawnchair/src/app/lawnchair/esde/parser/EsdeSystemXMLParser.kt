package app.lawnchair.esde.parser

import android.util.Xml
import androidx.annotation.ColorInt
import app.lawnchair.esde.data.EsdeSystemMetadata
import org.xmlpull.v1.XmlPullParser
import java.io.File
import androidx.core.graphics.toColorInt

object EsdeSystemXMLParser {
    fun parseEsdeSystemXml(xml: File): EsdeSystemMetadata? {
            if(xml.isFile) {
                val parser = Xml.newPullParser()
                parser.setInput(xml.inputStream(), null)

                var event = parser.eventType
                var inTheme = false
                var inVariables = false

                var systemName: String? = null
                var systemDescription: String? = null
                var systemManufacturer: String? = null
                var systemReleaseYear = 0
                var systemReleaseDate = ""
                var systemReleaseDateFormatted = ""
                var systemHardwareType = ""
                var systemCoverSize = ""
                var systemCartSize = ""
                var systemColor = ""

                var palette1 = ""
                var palette2 = ""
                var palette3 = ""
                var palette4 = ""

                while (event != XmlPullParser.END_DOCUMENT) {
                    when (event) {
                        XmlPullParser.START_TAG -> when (parser.name) {

                            "theme" -> inTheme = true

                            "variables" -> if (inTheme) {
                                inVariables = true
                            }

                            else -> if (inVariables) {
                                when (parser.name) {
                                    "systemName" -> systemName = readText(parser)
                                    "systemDescription" -> systemDescription = readText(parser)
                                    "systemManufacturer" -> systemManufacturer = readText(parser)
                                    "systemReleaseYear" ->
                                        systemReleaseYear = readText(parser).toIntOrNull() ?: 0
                                    "systemReleaseDate" ->
                                        systemReleaseDate = readText(parser)
                                    "systemReleaseDateFormated" ->
                                        systemReleaseDateFormatted = readText(parser)
                                    "systemHardwareType" ->
                                        systemHardwareType = readText(parser)
                                    "systemCoverSize" ->
                                        systemCoverSize = readText(parser)
                                    "systemCartSize" ->
                                        systemCartSize = readText(parser)
                                    "systemColor" ->
                                        systemColor = readText(parser)
                                    "systemColorPalette1" ->
                                        palette1 = readText(parser)
                                    "systemColorPalette2" ->
                                        palette2 = readText(parser)
                                    "systemColorPalette3" ->
                                        palette3 = readText(parser)
                                    "systemColorPalette4" ->
                                        palette4 = readText(parser)
                                }
                            }
                        }

                        XmlPullParser.END_TAG -> when (parser.name) {
                            "variables" -> inVariables = false
                            "theme" -> inTheme = false
                        }
                    }
                    event = parser.next()
                }

                val name = systemName ?: return null

                return EsdeSystemMetadata(
                    systemName = name,
                    systemDescription = systemDescription.orEmpty(),
                    systemManufacturer = systemManufacturer.orEmpty(),
                    systemReleaseYear = systemReleaseYear,
                    systemReleaseDate = systemReleaseDate,
                    systemReleaseDateFormatted = systemReleaseDateFormatted,
                    systemHardwareType = systemHardwareType,
                    systemCoverSize = systemCoverSize,
                    systemCartSize = systemCartSize,
                    systemColor = parseHexColorOrNull(systemColor),
                    primaryColor = parseHexColorOrNull(palette1),
                    secondaryColor = parseHexColorOrNull(palette2),
                    tertiaryColor = parseHexColorOrNull(palette3),
                    quaternaryColor = parseHexColorOrNull(palette4),
                )
        }
        return null
    }

    @ColorInt
    private fun parseHexColorOrNull(raw: String?): Int? {
        if (raw.isNullOrBlank()) return null

        val hex = raw.trim().removePrefix("#")

        return try {
            when (hex.length) {
                6, 8 -> "#$hex".toColorInt()
                else -> null
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun readText(parser: XmlPullParser): String {
        val result = StringBuilder()

        var event = parser.next()
        while (event != XmlPullParser.END_TAG) {
            when (event) {
                XmlPullParser.TEXT -> result.append(parser.text)
                XmlPullParser.START_TAG -> {
                    skipTag(parser)
                }
            }
            event = parser.next()
        }

        return result.toString().trim()
    }

    private fun skipTag(parser: XmlPullParser) {
        var depth = 1
        while (depth > 0) {
            when (parser.next()) {
                XmlPullParser.START_TAG -> depth++
                XmlPullParser.END_TAG -> depth--
            }
        }
    }
}
