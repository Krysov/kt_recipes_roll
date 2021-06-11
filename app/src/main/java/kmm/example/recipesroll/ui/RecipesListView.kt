package kmm.example.recipesroll.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import kmm.example.recipesroll.R
import java.util.concurrent.TimeUnit


class RecipesListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val itemFoldoutAnimationDurationMillis = context.resources
        .getInteger(R.integer.recipe_list_foldout_animation_duration_ms).toLong()

    fun setupListView(viewModel: RecipesListViewModel, forOwner: LifecycleOwner) {
        val adapter = RecipesListAdapter(viewModel, context)
        this.adapter = adapter
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        viewModel.onApplyViewModel(forOwner, adapter)
    }

    private fun RecipesListViewModel.onApplyViewModel(
        forOwner: LifecycleOwner,
        adapter: RecipesListAdapter
    ) {
        recipes.observe(forOwner, { adapter.setRecipes(it) })
        lastSelectedRecipe.observe(forOwner, {
            adapter.getPositionForRecipe(it)?.let { position ->
                Observable
                    .fromCallable { smoothScrollToItem(layoutManager, position) }
                    // invoke a second scroll to adjust for the changed item view height
                    .delay(itemFoldoutAnimationDurationMillis, TimeUnit.MILLISECONDS)
                    .subscribe { smoothScrollToItem(layoutManager, position) }
            }
        })
        fetchRecipes()
    }

    @SuppressLint("CheckResult")
    private fun smoothScrollToItem(layoutManager: LayoutManager?, position: Int) {
        layoutManager!!.startSmoothScroll(Scroller(this.context, position))
    }

    private class Scroller(val context: Context, toPosition: Int) : LinearSmoothScroller(context) {
        init {
            targetPosition = toPosition
        }

        override fun getVerticalSnapPreference() = SNAP_TO_START

        override fun calculateDyToMakeVisible(view: View?, snapPreference: Int): Int {
            return super.calculateDyToMakeVisible(view, snapPreference)
                .plus(context.resources.getDimension(R.dimen.recipe_item_cell_margin).toInt())
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return super.calculateSpeedPerPixel(displayMetrics) * 4.0f
        }
    }

}