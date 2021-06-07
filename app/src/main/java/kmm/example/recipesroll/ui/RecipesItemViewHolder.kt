package kmm.example.recipesroll.ui

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kmm.example.recipesroll.BR
import kmm.example.recipesroll.R
import kmm.example.recipesroll.databinding.RecipeItemBinding
import kmm.example.recipesroll.databinding.RecipeTagItemBinding
import kmm.example.recipesroll.model.RecipeModel


class RecipesItemViewHolder(
    private val binding: RecipeItemBinding,
    private val viewModel: RecipesViewModel,
) : RecyclerView.ViewHolder(binding.root) {

    private val thumbnailDownsizeFactor = 0.25

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
            false -> binding.setupPreview(recipe)
        }
    }

    private fun RecipeItemBinding.setupPreview(recipe: RecipeModel) {
        recipePhotoHero.visibility = GONE
        recipePhotoThumb.visibility = VISIBLE
        recipePhotoThumb.applyRecipePhoto(recipe, thumbnailDownsizeFactor)

        recipeDescription.visibility = GONE
        chefName.visibility = GONE
    }

    private fun RecipeItemBinding.setupDetail(recipe: RecipeModel) {
        recipePhotoThumb.visibility = GONE
        recipePhotoHero.visibility = VISIBLE
        recipePhotoHero.applyRecipePhoto(recipe)

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
                    .replace(root.context.getString(R.string.placeholder_name), name)
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

    private fun ImageView.applyRecipePhoto(
        recipe: RecipeModel,
        resizeResolution: Double = 1.0,
    ) {
        val resolution = resizeResolution.coerceAtMost(1.0)
        recipe.photo?.let { photo ->
            Picasso.get()
                .load(photo.url)
                .apply {
                    if (resolution < 1.0) resize(
                        (photo.details.dimen.width * resolution).toInt(),
                        (photo.details.dimen.height * resolution).toInt(),
                    )
                }
                .into(this)
        }
    }

}