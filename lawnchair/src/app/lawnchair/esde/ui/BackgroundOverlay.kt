package app.lawnchair.esde.ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible
import app.lawnchair.esde.data.BackgroundGameState
import app.lawnchair.esde.data.BackgroundState
import app.lawnchair.esde.data.BackgroundSystemState
import com.android.launcher3.InsettableFrameLayout
import com.android.launcher3.dragndrop.DragLayer

class BackgroundOverlay (context: Context) : FrameLayout(context) {

    private val imageView = ImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_CROP
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT,
        )
    }

    private val dimView = View(context).apply {
        setBackgroundColor(0x66000000) // 40% black
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT,
        )
    }

    private val iconImage = ImageView(context).apply {
        scaleType = ImageView.ScaleType.CENTER_INSIDE
        adjustViewBounds = true
        layoutParams = LayoutParams(
            175.dp,
            150.dp,
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER
            topMargin = (-15).dp //sure hope this doesn't cause issues
        }
    }

    private val metadataPanel = MetadataPanel(context)

    private val metadataHandle = View(context)

    init {
        addView(imageView)
        addView(dimView)
        addView(iconImage)

        //touch events keep getting caught by something within the launcher, this is a bruteforce solution to make the metadata panel work. There's probably a better alternative?
        metadataHandle.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    v.parent?.requestDisallowInterceptTouchEvent(true)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    v.parent?.requestDisallowInterceptTouchEvent(false)
                    metadataClickHandler()
                    true
                }

                MotionEvent.ACTION_CANCEL -> {
                    v.parent?.requestDisallowInterceptTouchEvent(false)
                    true
                }
                else -> false
            }
        }
        metadataPanel.visibility = GONE
    }

    var panelCurrentlyVisible : Boolean = false

    public fun addTouchRequiredViews(layer: DragLayer) {
        layer.addView(metadataPanel, getMetaDataPanelParams(null))
        layer.addView(metadataHandle, getMetaDataPanelParams(50.dp))
        metadataHandle.setBackgroundColor(Color.TRANSPARENT)
        metadataPanel.bringToFront()

        //don't know if all this is still needed now that we're doing absolute positioning
        metadataPanel.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    metadataPanel.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    val dragLayer = metadataPanel.parent as View

                    val topY = (dragLayer.height * 0.60f).toInt()

                    metadataPanel.translationX = 0f
                    metadataPanel.translationY = topY.toFloat()
                    metadataHandle.translationX = 0f
                    metadataHandle.translationY = topY.toFloat()
                }
            },
        )

        //We need a layer on top of the metadata panel and the elements within to catch touches in one place
        val touchCatcher = View(context).apply {
            setBackgroundColor(Color.TRANSPARENT)
            isClickable = true
            isFocusable = true

            setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_UP) {
                    metadataPanelClickHandler()
                }
                true
            }
        }

        metadataPanel.addView(touchCatcher)
        touchCatcher.bringToFront()
        metadataHandle.bringToFront()
    }

    //handle clicked -> open panel and start description scroll
    private fun metadataClickHandler() {
        if(metadataPanel.gameView.isVisible){
            metadataPanel.gameView.startAutoScroll()
        }
        metadataPanel.visibility = VISIBLE
        metadataHandle.visibility = GONE
        panelCurrentlyVisible = true
    }

    //panel clicked -> close panel and activate the invisible handle
    private fun metadataPanelClickHandler() {
        if(metadataPanel.gameView.isVisible){
            metadataPanel.gameView.stopAutoScroll()
        }
        metadataPanel.visibility = GONE
        metadataHandle.visibility = VISIBLE
        panelCurrentlyVisible = false
    }

    //the width here is weird because we override it depending on view size
    private fun getMetaDataPanelParams(widthProvided: Int?) : LayoutParams {
        val dm = resources.displayMetrics
        val width = widthProvided ?: (dm.widthPixels * 0.4f).toInt()
        val height = (dm.heightPixels * 0.25f).toInt()

        return InsettableFrameLayout.LayoutParams(width, height).apply {
            gravity = Gravity.START or Gravity.TOP
            isClickable = true
            isFocusable = true
        }
    }

    fun displayState(state: BackgroundState?) {
        when (state) {
            is BackgroundGameState.Game -> {
                displayGame(state)
            }
            is BackgroundSystemState.EsdeSystem -> {
                displaySystem(state)
            }
            else -> {
                clearBackground()
            }
        }
    }

    private fun setDrawable(drawable: Drawable?) {
        imageView.setImageDrawable(drawable)
    }

    private fun setDimAmount(alpha: Float) {
        dimView.alpha = alpha
    }

    private fun setIconDrawable(drawable: Drawable?) {
        iconImage.setImageDrawable(drawable)
    }

    private fun setBackgroundColor(color: Int?) {
        if (color == null) {
            imageView.setBackgroundColor(Color.TRANSPARENT)
        } else {
            imageView.setBackgroundColor(color)
        }
    }

    private fun displayGame(state: BackgroundGameState.Game) {
        setBackgroundColor(null)
        if(state.data != null) {
            setDimAmount(0.6f)
            setBackgroundImage(state.data.imagePath)
            setIconImage(state.data.iconPath)
            metadataPanel.showGame(state.data)
        }
    }

    private fun displaySystem(state: BackgroundSystemState.EsdeSystem) {
        setDrawable(state.image)
        setIconImage(null)
        if(state.metadata != null) {
            setDimAmount(0f)
            setBackgroundColor(state.metadata.systemColor)
            metadataPanel.showSystem(state.metadata)
        }
    }

    private fun setBackgroundImage(path: String?) {
        if (path == null) {
            setDrawable(null)
            return
        }
        setDrawable(Drawable.createFromPath(path))
    }

    private fun clearBackground() {
        setDrawable(null)
        setIconDrawable(null)
    }

    private fun setIconImage(path: String?) {
        if (path == null) {
            setIconDrawable(null)
            return
        }
        setIconDrawable(Drawable.createFromPath(path))
    }

    fun showMetadata() {
        if(panelCurrentlyVisible){
            metadataPanel.visibility = VISIBLE
            metadataHandle.visibility = GONE
        } else {
            metadataPanel.visibility = GONE
            metadataHandle.visibility = VISIBLE
        }
    }

    fun hideMetadata() {
        metadataPanel.visibility = GONE
        metadataHandle.visibility = GONE
    }


    companion object {
        fun attach(activity: Activity): BackgroundOverlay {
            val decor = activity.window.decorView as ViewGroup

            val overlay = BackgroundOverlay(activity).apply {
                layoutParams = ViewGroup.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT,
                )
            }

            decor.addView(overlay, 0)
            return overlay
        }
    }
}
