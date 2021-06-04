package kmm.example.recipesroll.model

data class RecipeModel(
    val title: String,
    val photo: PhotoModel,
    val calories: Int,
    val description: String,
    val chef: ChefModel = CHEF_NOT_ASSIGNED,
    val tags: List<TagModel> = emptyList(),
)