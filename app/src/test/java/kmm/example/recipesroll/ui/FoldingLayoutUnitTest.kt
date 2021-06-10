package kmm.example.recipesroll.ui

import android.content.Context
import android.view.View
import android.view.View.MeasureSpec.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class FoldingLayoutUnitTest {

    private val layoutWidth = 300
    private val layoutHeight = 200

    @Test
    fun `test foldout resize vertical`() {
        val layout = foldingLayout(LinearLayout.VERTICAL)

        layout.spread = 0.0f
        updateDimen(layout)
        assertEqualsDimen(300, 0, layout)

        layout.spread = 0.5f
        updateDimen(layout)
        assertEqualsDimen(300, 100, layout)

        layout.spread = 1.0f
        updateDimen(layout)
        assertEqualsDimen(300, 200, layout)
    }

    @Test
    fun `test foldout resize horizontal`() {
        val layout = foldingLayout(LinearLayout.HORIZONTAL)

        layout.spread = 0.0f
        updateDimen(layout)
        assertEqualsDimen(0, 200, layout)

        layout.spread = 0.5f
        updateDimen(layout)
        assertEqualsDimen(150, 200, layout)

        layout.spread = 1.0f
        updateDimen(layout)
        assertEqualsDimen(300, 200, layout)
    }

    @Test
    fun `test foldout resize edge cases`() {
        val layout = foldingLayout(LinearLayout.VERTICAL)

        layout.spread = -0.1f
        updateDimen(layout)
        assertEqualsDimen(300, -20, layout)

        layout.spread = 1.1f
        updateDimen(layout)
        assertEqualsDimen(300, 220, layout)
    }

    private fun updateDimen(layout: FoldingLayout) {
        layout.measure(
            makeMeasureSpec(400, AT_MOST),
            makeMeasureSpec(600, AT_MOST),
        )
    }

    private fun foldingLayout(layoutOrientation: Int): FoldingLayout {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val layout = FoldingLayout(context)
        layout.orientation = layoutOrientation
        layout.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

        val contentView = View(context)
        layout.addView(contentView)
        contentView.layoutParams = LinearLayout.LayoutParams(layoutWidth, layoutHeight)

        return layout
    }

    private fun assertEqualsDimen(
        expectedWidth: Int,
        expectedHeight: Int,
        layout: FoldingLayout,
    ) {
        Assert.assertEquals(expectedWidth, layout.measuredWidth)
        Assert.assertEquals(expectedHeight, layout.measuredHeight)
    }
}