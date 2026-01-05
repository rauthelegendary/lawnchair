package app.lawnchair.esde.ui

import android.content.Context
import android.widget.LinearLayout
import app.lawnchair.esde.data.EsdeSystemMetadata

class SystemMetadataView(context: Context) : LinearLayout(context) {

    private val deviceView = context.metadataText(18f)
    private val companyView = context.metadataText(14f)
    private val yearView = context.metadataText(14f)
    val panelWidthRatio = 0.40f

    init {
        orientation = VERTICAL
        setPadding(24.dp, 24.dp, 24.dp, 24.dp)

        addView(deviceView)
        addView(companyView)
        addView(yearView)
    }

    fun bind(system: EsdeSystemMetadata) {
        deviceView.text = system.systemName
        companyView.text = system.systemManufacturer
        yearView.text = system.systemReleaseYear.toString()
    }
}
