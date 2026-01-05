package app.lawnchair.esde.data

import androidx.annotation.ColorInt
import android.graphics.drawable.Drawable

sealed class BackgroundSystemState : BackgroundState {
    data class EsdeSystem(val metadata: EsdeSystemMetadata?, val image : Drawable?) :
        BackgroundSystemState()
}

data class EsdeSystemMetadata(
    val systemName: String,
    val systemDescription: String,
    val systemManufacturer: String,
    val systemReleaseYear: Int,
    val systemReleaseDate: String,
    val systemReleaseDateFormatted: String,
    val systemHardwareType: String,
    val systemCoverSize: String,
    @ColorInt val systemColor: Int?,
    @ColorInt val primaryColor: Int?,
    @ColorInt val secondaryColor: Int?,
    @ColorInt val tertiaryColor: Int?,
    @ColorInt val quaternaryColor: Int?,
    val systemCartSize: String,
)

