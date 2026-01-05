package app.lawnchair.esde.ui

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import app.lawnchair.esde.data.EsdeSystemMetadata
import app.lawnchair.esde.data.GameMetadata
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible

class MetadataPanel(context: Context) : FrameLayout(context) {

    val systemView = SystemMetadataView(context)
    val gameView = GameMetadataView(context)

    val panelAnimDuration = 250L
    val panelEasing = 0.85f

    init {
        setBackgroundColor("#CC202020".toColorInt())
        elevation = 8.dp.toFloat()

        addView(systemView)
        addView(gameView)

        systemView.visibility = View.GONE
        gameView.visibility = View.GONE
    }

    fun showSystem(system: EsdeSystemMetadata) {
        gameView.visibility = View.GONE
        systemView.visibility = View.VISIBLE
        updatePanelWidth(systemView.panelWidthRatio)
        systemView.bind(system)
    }

    fun showGame(game: GameMetadata) {
        systemView.visibility = View.GONE
        gameView.visibility = View.VISIBLE
        updatePanelWidth(gameView.panelWidthRatio)
        gameView.bind(game)
    }

    fun getCurrentViewWidth(): Int {
        if(systemView.isVisible) {
            return systemView.layoutParams.width
        }
        if(gameView.isVisible) {
            return gameView.layoutParams.width
        }
        return 0
    }

    private fun updatePanelWidth(ratio: Float) {
        val parent = this.parent as? View ?: return

        val screenWidth = parent.width
        if (screenWidth == 0) return

        this.layoutParams.width = (screenWidth * ratio).toInt()
    }
}
