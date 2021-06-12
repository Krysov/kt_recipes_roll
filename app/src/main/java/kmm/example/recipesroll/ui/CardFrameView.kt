package kmm.example.recipesroll.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import kmm.example.recipesroll.R.drawable.recipe_item_view_background
import kmm.example.recipesroll.R.drawable.recipe_item_view_frame


class CardFrameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val frameDrawable = AppCompatResources
        .getDrawable(context, recipe_item_view_frame)
    private val backgroundDrawable = AppCompatResources
        .getDrawable(context, recipe_item_view_background)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var deferredBitmap: Bitmap
    private lateinit var deferredCanvas: Canvas
    private lateinit var maskBitmap: Bitmap
    private lateinit var maskCanvas: Canvas
    private val maskXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    private var needMaskRedraw = true

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        background = backgroundDrawable
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw || h != oldh) {
            deferredBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            deferredCanvas = Canvas(deferredBitmap)

            maskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            maskCanvas = Canvas(maskBitmap)
            needMaskRedraw = true
        }
    }

    @SuppressLint("CanvasSize")
    override fun draw(canvas: Canvas) {
        super.draw(deferredCanvas)

        if (needMaskRedraw) {
            needMaskRedraw = false
            backgroundDrawable?.setBounds(0, 0, canvas.width, canvas.height)
            backgroundDrawable?.draw(maskCanvas)
        }

        paint.xfermode = maskXfermode
        deferredCanvas.drawBitmap(maskBitmap, .0f, .0f, paint)

        paint.xfermode = null
        canvas.drawBitmap(deferredBitmap, .0f, .0f, paint)

        frameDrawable?.setBounds(0, 0, canvas.width, canvas.height)
        frameDrawable?.draw(canvas)
    }

}