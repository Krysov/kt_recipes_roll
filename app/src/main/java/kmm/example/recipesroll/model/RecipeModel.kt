package kmm.example.recipesroll.model

import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.TransformQuery.*

/*
  The contentful framework is using reflection in java
  thus bypasses kotlin's null safety and default values.
  Hence any attribute must be nullable and set to null
  to allow for an empty constructor for data classes.
 */
@ContentfulEntryModel("recipe")
data class RecipeModel(
    @ContentfulSystemField val id: String? = null,
    @ContentfulField val title: String? = null,
    @ContentfulField("photo") private val photoAsset: CDAAsset? = null,
    @ContentfulField val calories: Double? = null,
    @ContentfulField val description: String? = null,
    @ContentfulField val chef: ChefModel? = null,
    @ContentfulField val tags: List<TagModel>? = null,
    @Transient val selected: Boolean = false,
) {

    val photo: PhotoModel?
        get() = photoAsset?.let { PhotoModel(it) }
}