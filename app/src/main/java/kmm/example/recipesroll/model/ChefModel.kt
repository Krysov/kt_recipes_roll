package kmm.example.recipesroll.model

import com.contentful.java.cda.TransformQuery.ContentfulEntryModel
import com.contentful.java.cda.TransformQuery.ContentfulField


/*
  The contentful framework is using reflection in java
  thus bypasses kotlin's null safety and default values.
  Hence any attribute must be nullable and set to null
  to allow for an empty constructor for data classes.
 */
@ContentfulEntryModel("chef")
data class ChefModel(
    @ContentfulField val name: String? = null,
)