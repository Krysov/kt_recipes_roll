package kmm.example.recipesroll.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kmm.example.recipesroll.model.RecipeModel
import kmm.example.recipesroll.remote.RecipesApi
import timber.log.Timber


class RecipesViewModel(private val api: RecipesApi) : ViewModel() {

    val recipes: MutableLiveData<List<RecipeModel>> = MutableLiveData()
    val lastSelectedRecipe: MutableLiveData<RecipeModel> = MutableLiveData()
    private var indexedRecipes = mutableMapOf<String, RecipeModel>()

    fun updateRecipes() {
        api.fetchRecipes(
            { syncRecipes(it) },
            { Timber.d(it) },
        )
    }

    fun select(recipe: RecipeModel) {
        setSelection(recipe, true)
        lastSelectedRecipe.value = indexedRecipes[recipe.id]
    }

    fun deselect(recipe: RecipeModel) = setSelection(recipe, false)

    private fun setSelection(recipe: RecipeModel, selected: Boolean) {
        indexedRecipes[recipe.id!!] = recipe.copy(selected = selected)
        recipes.value = recipes.value?.map { indexedRecipes[it.id!!]!! }
    }

    private fun syncRecipes(recipes: Collection<RecipeModel>) {
        val newRecipes: List<RecipeModel> = recipes.map {
            when (val old = indexedRecipes[it.id]) {
                null -> it
                else -> it.copy(selected = old.selected)
            }
        }
        indexedRecipes = newRecipes.associateBy { it.id!! }.toMutableMap()
        this.recipes.value = newRecipes
    }

}