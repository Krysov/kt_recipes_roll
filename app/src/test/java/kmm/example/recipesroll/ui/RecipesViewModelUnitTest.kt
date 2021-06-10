package kmm.example.recipesroll.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.internal.schedulers.ExecutorScheduler.ExecutorWorker
import io.reactivex.plugins.RxJavaPlugins
import kmm.example.recipesroll.remote.DummyRecipesApi
import kmm.example.recipesroll.utils.AnimationPacer
import kmm.example.recipesroll.utils.AnimationProgression
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.math.roundToInt


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
        vm.fetchRecipes()
        assertEquals(1, numUpdateCalls)
        assertEquals(4, numReturnedItems)

        api.recipesCount = 8
        vm.fetchRecipes()
        assertEquals(2, numUpdateCalls)
        assertEquals(8, numReturnedItems)

        api.recipesCount = 0
        vm.fetchRecipes()
        assertEquals(3, numUpdateCalls)
        assertEquals(0, numReturnedItems)

        api.simulateConnectionError = true
        vm.fetchRecipes()
        assertEquals(3, numUpdateCalls)
        assertEquals(0, numReturnedItems)
    }

    @Test
    fun `test item selection`() {
        var numRecipesUpdate = 0
        val api = DummyRecipesApi
        val vm = RecipesViewModel(api)
            .apply { recipes.observeForever { numRecipesUpdate++ } }
        assertEquals(0, numRecipesUpdate)

        api.recipesCount = 4
        vm.fetchRecipes()
        val model = vm.modelAt(1)
        assertEquals(false, model.selected)
        assertEquals(null, vm.lastSelectedRecipe.value?.id)
        assertEquals(1, numRecipesUpdate)

        vm.select(model)
        assertEquals(true, vm.modelAt(1).selected)
        assertEquals(model.id, vm.lastSelectedRecipe.value?.id)
        assertEquals(2, numRecipesUpdate)

        vm.deselect(model)
        assertEquals(false, vm.modelAt(1).selected)
        assertEquals(model.id, vm.lastSelectedRecipe.value?.id)
        assertEquals(3, numRecipesUpdate)

        vm.select(vm.modelAt(2))
        api.recipesCount = 8
        vm.fetchRecipes()
        assertEquals(true, vm.modelAt(2).selected)
        assertEquals(vm.modelAt(2).id, vm.lastSelectedRecipe.value?.id)
        assertEquals(5, numRecipesUpdate)

        vm.select(vm.modelAt(6))
        api.recipesCount = 4
        vm.fetchRecipes()
        var gotExpectedException = false
        try {
            assertEquals(false, vm.modelAt(6).selected)
        } catch (e: java.lang.IndexOutOfBoundsException) {
            gotExpectedException = true
        }
        assert(gotExpectedException)
        assertEquals(true, vm.modelAt(2).selected)
        assertEquals(7, numRecipesUpdate)
    }

    @Test(timeout = 5000)
    fun `test animation queuing`() {
        var nestedError: Throwable? = null
        val timer = object : AnimationPacer() {
            var callback: ((withMillis: Long) -> Unit)? = null
            fun update(withMillis: Long) = callback?.let { it(withMillis) }

            override fun scheduleUpdate(
                afterTimeDelayMillis: Long,
                callback: (passedTimeMillis: Long) -> Unit
            ) = run { this.callback = callback }
        }
        val vm = RecipesViewModel(DummyRecipesApi, timer).apply { fetchRecipes() }
        val ap = object : AnimationProgression<Float>(300) {
            override fun onComputeAnimatedValueInterpolation(progress: Float) = 20 * (1 - progress)
        }

        Observable.zip(
            vm.animationAt(1),
            Observable.fromCallable { vm.select(vm.modelAt(1), ap) },
            { animProgression, _ -> animProgression }
        ).blockingSubscribe({ animProgression ->
            assertEquals(0.0f, animProgression.progress, 10)
            assertEquals(20.0f, animProgression.interpolation, 10)
        }, { nestedError = it })

        Observable.zip(
            vm.animationAt(1),
            Observable.fromCallable { timer.update(150) },
            { animProgression, _ -> animProgression }
        ).blockingSubscribe({ animProgression ->
            assertEquals(0.5f, animProgression.progress, 10)
            assertEquals(10.0f, animProgression.interpolation, 10)
        }, { nestedError = it })

        Observable.zip(
            vm.animationAt(1),
            Observable.fromCallable { timer.update(150) },
            { animProgression, _ -> animProgression }
        ).blockingSubscribe({ animProgression ->
            assertEquals(1.0f, animProgression.progress, 10)
            assertEquals(0.0f, animProgression.interpolation, 10)
        }, { nestedError = it })

        var receivedTimeoutAsExpected = false
        Observable.zip(
            vm.animationAt(1),
            Observable.fromCallable { timer.update(1) },
            { animProgression, _ -> animProgression }
        ).timeout(10, TimeUnit.MILLISECONDS)
            .blockingSubscribe({
                assert(false) { "There should not be any more updates!" }
            }, {
                if (it is TimeoutException) receivedTimeoutAsExpected = true
            })
        assert(receivedTimeoutAsExpected) { "Did not receive timeout as expected!" }

        assert(nestedError == null) { nestedError!! }
    }

    private fun RecipesViewModel.animationAt(index: Int) =
        getAnimationUpdates(modelAt(index))

    private fun RecipesViewModel.modelAt(index: Int) =
        recipes.value?.elementAt(index)
            ?: throw IndexOutOfBoundsException()

    private fun assertEquals(expected: Float, actual: Float, precisionToDecimalPoint: Int) {
        assertEquals(
            (expected * precisionToDecimalPoint).roundToInt(),
            (actual * precisionToDecimalPoint).roundToInt()
        )
    }

    @Before
    fun setUpRxSchedulers() {
        val immediate: Scheduler = object : Scheduler() {
            override fun scheduleDirect(
                run: Runnable, delay: Long, unit: TimeUnit
            ) = super.scheduleDirect(run, 0, unit)

            override fun createWorker() =
                ExecutorWorker({ obj: Runnable -> obj.run() }, false)
        }
        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
    }

}