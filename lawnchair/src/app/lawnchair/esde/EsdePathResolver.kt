package app.lawnchair.esde

import android.os.Environment
import java.io.File

object EsdePathResolver {
    private val IMAGE_SOURCES = arrayOf("fanart", "screenshots", "covers")
    private val EXTENSIONS = arrayOf("png", "jpg", "jpeg", "webp")
    private val BASE_PATH = Environment.getExternalStorageDirectory().path + "/ES-DE/downloaded_media/"
    private val SYSTEM_XML_PATH = Environment.getExternalStorageDirectory().path + "/ES-DE/lawnchair/systems/metadata-global/"
    private val SYSTEM_IMAGE_PATH = Environment.getExternalStorageDirectory().path +"/ES-DE/lawnchair/systems/images/"
    private val GAMELIST_PATH = Environment.getExternalStorageDirectory().path +"/ES-DE/gamelists/"


    public fun getGamelistPath(): String {
        return GAMELIST_PATH
    }

    public fun getSystemXmlPath(): String {
        return SYSTEM_XML_PATH
    }

    public fun resolveImagePathMultipleSources(system: String?, title: String?): String? {
        for (source in IMAGE_SOURCES) {
            val file = resolveImagePath( concatImagePath(system, source, title))
            if (file != null) return file
        }
        return null
    }

    public fun resolveImagePath(system: String, source: String, title: String): String? {
        for (ext in EXTENSIONS) {
            val path = concatImagePath(system, source, title)
            val file = File("$BASE_PATH$path.$ext")
            if (file.exists()) return file.path
        }
        return null
    }

    public fun resolveSystemImagePath(system: String): String? {
        for (ext in EXTENSIONS) {
            val file = File("$SYSTEM_IMAGE_PATH$system.$ext")
            if (file.exists()) return file.path
        }
        return null
    }

    public fun resolveSystemXMLPath(system: String) : String? {
        val file = File("$SYSTEM_XML_PATH$system.xml")
        if (file.exists()) return file.path
        return null
    }

    private fun resolveImagePath(basePath: String): String? {
        for (ext in EXTENSIONS) {
            val file = File("$BASE_PATH$basePath.$ext")
            if (file.exists()) return file.path
        }
        return null
    }

    private fun concatImagePath(system: String?, source: String?,  title: String?): String {
        return "$system/$source/$title"
    }
}
