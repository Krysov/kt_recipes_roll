package kmm.example.recipesroll.model

import com.contentful.java.cda.TransformQuery.ContentfulEntryModel
import com.contentful.java.cda.TransformQuery.ContentfulField


// the contentful framework is using reflection in java and
// ignores kotlin's non-nullable types and default values
// hence any attribute must be nullable and set to null
// to allow for an empty constructor
@ContentfulEntryModel("chef")
data class ChefModel(
    @ContentfulField val name: String? = null,
)