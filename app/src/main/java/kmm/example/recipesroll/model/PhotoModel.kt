package kmm.example.recipesroll.model

import com.contentful.java.cda.CDAAsset

/*
  Convenience wrapper for contentful's image assets.
 */
class PhotoModel(private val _photo: CDAAsset) {

    val details: ImageDetails by lazy {
        ImageDetails(_photo.fileField("details"))
    }

    val url: String
        get() = _photo.url().let {
            if (it.startsWith("//")) return@let "https:$it"
            else return@let it
        }

    class ImageDetails(detailFields: Map<String, Any>) {
        val size: Double = detailFields["size"] as Double
        val dimen = ImageDimen(
            width = getImageDimen(detailFields, "width"),
            height = getImageDimen(detailFields, "height"),
        )

        private fun getImageDimen(fields: Map<String, Any>, key: String) =
            (fields["image"] as Map<*, *>)[key]!! as Double
    }

    class ImageDimen(
        val width: Double,
        val height: Double,
    )
}