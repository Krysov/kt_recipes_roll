package kmm.example.recipesroll.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kmm.example.recipesroll.BR
import kmm.example.recipesroll.databinding.RecipeItemBinding
import kmm.example.recipesroll.model.RecipeModel


class RecipesListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    class RecipesListAdapter : RecyclerView.Adapter<RecipesItemViewHolder>() {

        private var recipes: Collection<RecipeModel> = emptyList()

        fun setRecipes(recipes: Collection<RecipeModel>) {
            this.recipes = recipes
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RecipeItemBinding.inflate(inflater, parent, false)
            return RecipesItemViewHolder(binding)
        }

        override fun onBindViewHolder(holder: RecipesItemViewHolder, position: Int) {
            holder.init(recipes.elementAt(position))
        }

        override fun getItemCount(): Int = recipes.size
    }


    class RecipesItemViewHolder(
        private val binding: ViewDataBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun init(recipe: RecipeModel) {
            binding.setVariable(BR.recipe, recipe)
            binding.executePendingBindings()
        }
    }

}