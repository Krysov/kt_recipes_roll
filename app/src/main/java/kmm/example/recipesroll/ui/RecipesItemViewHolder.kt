package kmm.example.recipesroll.ui

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.noties.markwon.Markwon
import io.reactivex.disposables.Disposable
import kmm.example.recipesroll.BR
import kmm.example.recipesroll.R
import kmm.example.recipesroll.databinding.RecipeItemBinding
import kmm.example.recipesroll.databinding.RecipeTagItemBinding
import kmm.example.recipesroll.model.RecipeModel
import kmm.example.recipesroll.utils.AnimationProgression
import timber.log.Timber


class RecipesItemViewHolder(
    private val binding: RecipeItemBinding,
    private val viewModel: RecipesViewModel,
) : RecyclerView.ViewHolder(binding.root) {

    private val animationDurationMillis = 250L
    private val thumbnailDownsizeFactor = 0.25
    private var animationUpdates: Disposable? = null

    fun init(recipe: RecipeModel) {
        binding.setVariable(BR.recipe, recipe)
        binding.executePendingBindings()
        binding.root.setOnClickListener { onClick(recipe) }
        binding.recipeDescription.setOnClickListener { onClick(recipe) }
        binding.tagContainerView.removeAllViews()
        binding.recipePhotoThumb.applyRecipePhoto(recipe, thumbnailDownsizeFactor)
        when (recipe.selected) {
            true -> binding.setupDetail(recipe)
            false -> binding.setupPreview()
        }
        animationUpdates?.dispose()
        animationUpdates = viewModel.getAnimationUpdates(recipe)
            .subscribe(this::applyAnimation) { Timber.e(it) }
    }

    private fun applyAnimation(animation: AnimationProgression<Float>) {
        binding.setFoldoutExtent(animation.interpolation)
    }

    fun release() {
        animationUpdates?.dispose()
    }

    private fun onClick(recipe: RecipeModel) {
        when (recipe.selected) {
            true -> viewModel.deselect(recipe,
                AnimationProgression.createAnimation(animationDurationMillis) { progress -> 1 - progress })
            false -> viewModel.select(recipe,
                AnimationProgression.createAnimation(animationDurationMillis) { it })
        }
    }

    private fun RecipeItemBinding.setupPreview() {
        setFoldoutExtent(0.0f)
        recipePhotoHero.setImageDrawable(null)
    }

    private fun RecipeItemBinding.setupDetail(recipe: RecipeModel) {
        setFoldoutExtent(1.0f)
        recipePhotoHero.applyRecipePhoto(recipe)

        recipe.description?.let { text ->
            Markwon.create(recipeDescription.context)
                .setMarkdown(recipeDescription, text)
        }

        setupChefName(recipe)
        populateTagContainer(recipe)
    }

    private fun RecipeItemBinding.setupChefName(recipe: RecipeModel) {
        when (val name = recipe.chef?.name) {
            null -> chefName.visibility = GONE
            else -> {
                chefName.visibility = VISIBLE
                chefName.text = root.context
                    .getString(R.string.recipe_list_item_chef_template)
                    .replace(root.context.getString(R.string.placeholder_name), name)
            }
        }
    }

    private fun RecipeItemBinding.setFoldoutExtent(extent: Float) {
        folderPhotoThumb.spread = 1 - extent
        folderPhotoHero.spread = extent
        folderDetails.spread = extent
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
                .priority(Picasso.Priority.HIGH)
                .into(this)
        }
    }

}