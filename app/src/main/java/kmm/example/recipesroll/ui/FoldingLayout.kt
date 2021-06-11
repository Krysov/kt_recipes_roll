package kmm.example.recipesroll.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View.MeasureSpec.*
import android.widget.LinearLayout
import kotlin.math.abs


class FoldingLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val visibilityThresholdPx = 1

    var spread: Float = 1.0f
        set(value) {
            field = value.coerceAtLeast(0.0f)
            requestLayout()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val unrestricted = makeMeasureSpec(0, UNSPECIFIED)
        when (orientation) {
            HORIZONTAL -> super.onMeasure(unrestricted, heightMeasureSpec)
            else -> super.onMeasure(widthMeasureSpec, unrestricted) // default VERTICAL
        }
        val adjustedWidth: Int
        val adjustedHeight: Int
        when (orientation) {
            HORIZONTAL -> {
                adjustedWidth = (measuredWidth.toFloat() * spread).toInt()
                adjustedHeight = measuredHeight
                updateVisibility(adjustedWidth)
            }
            else -> { // default VERTICAL
                adjustedWidth = measuredWidth
                adjustedHeight = (measuredHeight.toFloat() * spread).toInt()
                updateVisibility(adjustedHeight)
            }
        }
        super.onMeasure(
            makeMeasureSpec(adjustedWidth, EXACTLY),
            makeMeasureSpec(adjustedHeight, EXACTLY),
        )
    }

    private fun updateVisibility(basedOnSize: Int) {
        visibility = if (abs(basedOnSize) >= visibilityThresholdPx) VISIBLE else INVISIBLE
    }

}