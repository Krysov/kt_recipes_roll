package kmm.example.recipesroll.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kmm.example.recipesroll.R


class RecipesListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    fun setupListView(viewModel: RecipesViewModel, forOwner: LifecycleOwner) {
        val adapter = RecipesListAdapter(viewModel, context)
        this.adapter = adapter
        layoutManager = LinearLayoutManager(context, VERTICAL, false)

        viewModel.recipes.observe(forOwner, { adapter.setRecipes(it) })
        viewModel.lastSelectedRecipe.observe(forOwner, {
            adapter.getPositionForRecipe(it)?.let { position ->
                layoutManager!!.startSmoothScroll(Scroller(this.context, position))
            }
        })
        viewModel.fetchRecipes()
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
    }

}