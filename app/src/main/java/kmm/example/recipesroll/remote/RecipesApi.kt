package kmm.example.recipesroll.remote

import kmm.example.recipesroll.model.RecipeModel

interface RecipesApi {
    fun fetchRecipes(): List<RecipeModel>
}