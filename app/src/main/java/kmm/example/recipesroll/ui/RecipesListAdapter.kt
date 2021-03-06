package kmm.example.recipesroll.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kmm.example.recipesroll.R
import kmm.example.recipesroll.databinding.RecipeItemBinding
import kmm.example.recipesroll.model.RecipeModel


class RecipesListAdapter(
    private val viewModel: RecipesListViewModel,
    context: Context,
) : RecyclerView.Adapter<RecipesListItemViewHolder>() {

    private val itemSpacing = context.resources
        .getDimension(R.dimen.recipe_item_cell_margin).toInt()
    private var recipes: Collection<RecipeModel> = emptyList()

    fun setRecipes(recipes: Collection<RecipeModel>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    fun getPositionForRecipe(forRecipe: RecipeModel): Int? {
        recipes.forEachIndexed { i, recipe ->
            if (forRecipe.id.equals(recipe.id)) return i
        }
        return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeItemBinding.inflate(inflater, parent, false)
        return RecipesListItemViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: RecipesListItemViewHolder, position: Int) {
        val params = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
        if (position == 0) {
            params.topMargin = itemSpacing
        } else params.topMargin = 0
        params.bottomMargin = itemSpacing
        holder.itemView.layoutParams = params
        holder.init(recipes.elementAt(position))
    }

    override fun onViewRecycled(holder: RecipesListItemViewHolder) = holder.release()

    override fun getItemCount(): Int = recipes.size
}