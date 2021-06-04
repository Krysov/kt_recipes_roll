package kmm.example.recipesroll.model

import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.TransformQuery.ContentfulEntryModel
import com.contentful.java.cda.TransformQuery.ContentfulField


// the contentful framework is using reflection in java and
// ignores kotlin's non-nullable types and default values
// hence any attribute must be nullable and set to null
// to allow for an empty constructor
@ContentfulEntryModel("recipe")
data class RecipeModel(
    @ContentfulField val title: String? = null,
    @ContentfulField val photo: CDAAsset? = null,
    @ContentfulField val calories: Double? = null,
    @ContentfulField val description: String? = null,
    @ContentfulField val chef: ChefModel? = null,
    @ContentfulField val tags: List<TagModel>? = null,
)