package kmm.example.recipesroll.model

import com.contentful.java.cda.TransformQuery.ContentfulEntryModel
import com.contentful.java.cda.TransformQuery.ContentfulField


@ContentfulEntryModel("chef")
data class ChefModel(
    @ContentfulField val name: String? = null,
)