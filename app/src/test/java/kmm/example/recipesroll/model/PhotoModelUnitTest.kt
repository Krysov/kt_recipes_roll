package kmm.example.recipesroll.model

import kmm.example.recipesroll.remote.DummyRecipesApi
import kmm.example.recipesroll.remote.RemoteRecipesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.net.URI


@RunWith(JUnit4::class)
class PhotoModelUnitTest {

    @Test
    fun `test wrapper getters validity with test data`() {
        IntRange(0, 4).forEach { i ->
            val photo = PhotoModel(DummyRecipesApi.getPhotoData(i))
            assertValidity(photo)
        }
    }

    @Test(timeout = 5000)
    fun `test wrapper getters validity with live data`() {
        RemoteRecipesApi.fetchRecipes().forEach { recipe ->
            assertValidity(recipe.photo!!)
        }
    }

    private fun assertValidity(photo: PhotoModel) {
        val invalidDimen = "Invalid Photo Dimensions for: $photo"
        assert(photo.details.size > 0) { invalidDimen }
        assert(photo.details.dimen.width > 0) { invalidDimen }
        assert(photo.details.dimen.height > 0) { invalidDimen }
        assert(URI.create(photo.url).toURL() != null) { "Invalid URL:${photo.url}" }
    }

}