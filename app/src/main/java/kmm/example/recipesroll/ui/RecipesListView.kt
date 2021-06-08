package kmm.example.recipesroll.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kmm.example.recipesroll.R
import kmm.example.recipesroll.databinding.RecipeItemBinding
import kmm.example.recipesroll.model.RecipeModel


class RecipesListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    fun applyViewModel(viewModel: RecipesViewModel, forOwner: LifecycleOwner) {
        val adapter = RecipesListAdapter(viewModel, context)
        this.adapter = adapter
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        viewModel.recipes.observe(forOwner, { adapter.setRecipes(it) })
        viewModel.updateRecipes()
    }

    private class RecipesListAdapter(
        private val viewModel: RecipesViewModel,
        context: Context,
    ) : RecyclerView.Adapter<RecipesItemViewHolder>() {

        private val itemSpacing = context.resources
            .getDimension(R.dimen.recipe_item_cell_margin).toInt()

        private var recipes: Collection<RecipeModel> = emptyList()

        fun setRecipes(recipes: Collection<RecipeModel>) {
            this.recipes = recipes
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RecipeItemBinding.inflate(inflater, parent, false)
            return RecipesItemViewHolder(binding, viewModel)
        }

        override fun onBindViewHolder(holder: RecipesItemViewHolder, position: Int) {
            val params = holder.itemView.layoutParams as MarginLayoutParams
            if (position == 0) {
                params.topMargin = itemSpacing
            } else params.topMargin = 0
            params.bottomMargin = itemSpacing
            holder.itemView.layoutParams = params
            holder.init(recipes.elementAt(position))
        }

        override fun getItemCount(): Int = recipes.size
    }
}