package app.lawnchair.esde.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ScrollView
import app.lawnchair.esde.data.GameMetadata

class GameMetadataView(context: Context) : FrameLayout(context) {

    private val descriptionView = context.metadataText(14f)
    val panelWidthRatio = 0.60f

    private val scrollView = ScrollView(context).apply {
        isClickable = false
        isFocusable = false
        isVerticalScrollBarEnabled = false
        overScrollMode = View.OVER_SCROLL_NEVER
    }
    private var scrollAnimator: ValueAnimator? = null
    private val handler = Handler(Looper.getMainLooper())

    private val scrollResetTime: Long = 3000L
    private val scrollStartTime: Long = 4000L

    init {
        setPadding(18.dp, 18.dp, 18.dp, 18.dp)
        scrollView.addView(descriptionView)
        scrollView.isEnabled = false
        addView(scrollView)
    }

    fun bind(game: GameMetadata) {
        descriptionView.text = game.description.orEmpty()
        startAutoScroll()
    }

    fun startAutoScroll() {
        stopAutoScroll()
        scrollView.scrollTo(0, 0)

        scrollView.post {
            val maxScroll = scrollView.getChildAt(0).height - scrollView.height
            if (maxScroll <= 0) return@post

            //start initial scroll after delay
            handler.postDelayed({
                scrollAnimator = ValueAnimator.ofInt(0, maxScroll).apply {
                    duration = maxScroll * 55L
                    interpolator = LinearInterpolator()

                    addUpdateListener {
                        scrollView.scrollTo(0, it.animatedValue as Int)
                    }

                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            //restart scroll after delay
                            handler.postDelayed({
                                startAutoScroll()
                            }, scrollResetTime)
                        }
                    })

                    start()
                }},scrollStartTime)
        }
    }

    fun stopAutoScroll() {
        scrollAnimator?.cancel()
        scrollAnimator = null
        handler.removeCallbacksAndMessages(null)
    }
}
