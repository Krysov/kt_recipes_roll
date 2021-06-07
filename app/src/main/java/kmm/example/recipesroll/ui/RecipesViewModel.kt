package kmm.example.recipesroll.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kmm.example.recipesroll.model.RecipeModel
import kmm.example.recipesroll.remote.RecipesApi
import timber.log.Timber

class RecipesViewModel(private val api: RecipesApi) : ViewModel() {
    private val recipes: MutableLiveData<Collection<RecipeModel>> = MutableLiveData()
    fun getRecipes() = recipes

    fun fetchRecipes() {
        api.fetchRecipes(
            { recipes.value = it },
            { Timber.d(it) }
        )
    }
}