package kmm.example.recipesroll.remote

import kmm.example.recipesroll.model.RecipeModel


interface RecipesApi {
    fun fetchRecipes(
        onResult: (Collection<RecipeModel>) -> Unit,
        onError: (Throwable) -> Unit,
    )
}