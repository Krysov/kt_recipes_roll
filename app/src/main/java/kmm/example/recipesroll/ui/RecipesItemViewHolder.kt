package kmm.example.recipesroll.ui

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import kmm.example.recipesroll.BR
import kmm.example.recipesroll.R
import kmm.example.recipesroll.databinding.RecipeItemBinding
import kmm.example.recipesroll.databinding.RecipeTagItemBinding
import kmm.example.recipesroll.model.RecipeModel


class RecipesItemViewHolder(
    private val binding: RecipeItemBinding,
    private val viewModel: RecipesViewModel,
) : RecyclerView.ViewHolder(binding.root) {

    fun init(recipe: RecipeModel) {
        binding.setVariable(BR.recipe, recipe)
        binding.executePendingBindings()
        binding.root.setOnClickListener {
            if (recipe.selected) viewModel.deselect(recipe)
            else viewModel.select(recipe)
        }
        binding.tagContainerView.removeAllViews()
        when (recipe.selected) {
            true -> binding.setupDetail(recipe)
            false -> binding.setupPreview()
        }
    }

    private fun RecipeItemBinding.setupPreview() {
        recipePhotoThumb.visibility = VISIBLE
        recipePhotoHero.visibility = GONE

        recipeDescription.visibility = GONE
        chefName.visibility = GONE
    }

    private fun RecipeItemBinding.setupDetail(recipe: RecipeModel) {
        recipePhotoThumb.visibility = GONE
        recipePhotoHero.visibility = VISIBLE

        recipeDescription.visibility = VISIBLE
        recipe.description?.let { description ->
            recipeDescription.text = description
        }

        when (val name = recipe.chef?.name) {
            null -> chefName.visibility = GONE
            else -> {
                chefName.visibility = VISIBLE
                chefName.text = root.context
                    .getString(R.string.recipe_list_item_chef_template)
                    .replace("NAME", name)
            }
        }

        populateTagContainer(recipe)
    }

    private fun RecipeItemBinding.populateTagContainer(recipe: RecipeModel) {
        recipe.tags?.forEach { tag ->
            val inflater = LayoutInflater.from(tagContainerView.context)
            RecipeTagItemBinding.inflate(inflater, tagContainerView, false).let {
                it.setVariable(BR.tag, tag)
                it.executePendingBindings()
                tagContainerView.addView(it.root)
            }
        }
    }

}