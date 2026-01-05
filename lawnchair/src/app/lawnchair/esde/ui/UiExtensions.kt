package app.lawnchair.esde.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView

val Int.dp: Int
    get() = (this * ResourcesHolder.density).toInt()

object ResourcesHolder {
    var density: Float = 1f
}

fun initUiUtils(context: Context) {
    ResourcesHolder.density = context.resources.displayMetrics.density
}

fun Context.metadataText(
    sizeSp: Float,
    bold: Boolean = false
): TextView =
    TextView(this).apply {
        setTextColor(Color.WHITE)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp)
        if (bold) setTypeface(typeface, Typeface.BOLD)
        setLineSpacing(0f, 1.1f)
    }
