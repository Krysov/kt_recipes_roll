package kmm.example.recipesroll.model

import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.TransformQuery.ContentfulEntryModel
import com.contentful.java.cda.TransformQuery.ContentfulField


@ContentfulEntryModel("recipe")
class RecipeModel(
    @ContentfulField val title: String? = null,
    @ContentfulField val photo: CDAAsset? = null,
    @ContentfulField val calories: Double? = null,
    @ContentfulField val description: String? = null,
    @ContentfulField val chef: ChefModel? = null,
    @ContentfulField val tags: List<TagModel>? = null,
)