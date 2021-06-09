package kmm.example.recipesroll.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kmm.example.recipesroll.model.RecipeModel
import kmm.example.recipesroll.remote.DummyRecipesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class RecipesViewModelUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `test list updates`() {
        var numUpdateCalls = 0
        var numReturnedItems = 0
        val api = DummyRecipesApi
        val vm = RecipesViewModel(api)
        vm.recipes.observeForever {
            numUpdateCalls++
            numReturnedItems = it.count()
        }

        api.recipesCount = 4
        vm.updateRecipes()
        assertEquals(1, numUpdateCalls)
        assertEquals(4, numReturnedItems)

        api.recipesCount = 8
        vm.updateRecipes()
        assertEquals(2, numUpdateCalls)
        assertEquals(8, numReturnedItems)

        api.recipesCount = 0
        vm.updateRecipes()
        assertEquals(3, numUpdateCalls)
        assertEquals(0, numReturnedItems)

        api.simulateConnectionError = true
        vm.updateRecipes()
        assertEquals(3, numUpdateCalls)
        assertEquals(0, numReturnedItems)
    }

    @Test
    fun `test item selection`() {
        val api = DummyRecipesApi
        val vm = RecipesViewModel(api)
        val modelAt: (Int) -> RecipeModel = { index ->
            vm.recipes.value?.elementAt(index)
                ?: throw IndexOutOfBoundsException()
        }

        api.recipesCount = 4
        vm.updateRecipes()
        val model = modelAt(1)
        assertEquals(false, model.selected)
        assertEquals(null, vm.lastSelectedRecipe.value?.id)

        vm.select(model)
        assertEquals(true, modelAt(1).selected)
        assertEquals(model.id, vm.lastSelectedRecipe.value?.id)

        vm.deselect(model)
        assertEquals(false, modelAt(1).selected)
        assertEquals(model.id, vm.lastSelectedRecipe.value?.id)

        vm.select(modelAt(2))
        api.recipesCount = 8
        vm.updateRecipes()
        assertEquals(true, modelAt(2).selected)
        assertEquals(modelAt(2).id, vm.lastSelectedRecipe.value?.id)

        vm.select(modelAt(6))
        api.recipesCount = 4
        vm.updateRecipes()
        var gotExpectedException = false
        try {
            assertEquals(false, modelAt(6).selected)
        } catch (e: java.lang.IndexOutOfBoundsException) {
            gotExpectedException = true
        }
        assert(gotExpectedException)
        assertEquals(true, modelAt(2).selected)
    }

}