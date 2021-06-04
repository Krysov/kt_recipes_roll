package kmm.example.recipesroll.model

import com.contentful.java.cda.TransformQuery.ContentfulEntryModel
import com.contentful.java.cda.TransformQuery.ContentfulField


@ContentfulEntryModel("tag")
data class TagModel(
    @ContentfulField val name: String? = null,
)