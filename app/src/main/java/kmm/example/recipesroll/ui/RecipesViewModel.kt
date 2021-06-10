package kmm.example.recipesroll.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kmm.example.recipesroll.model.RecipeModel
import kmm.example.recipesroll.remote.RecipesApi
import kmm.example.recipesroll.utils.AnimationPacer
import kmm.example.recipesroll.utils.AnimationProgression
import timber.log.Timber


class RecipesViewModel(
    private val api: RecipesApi,
    private val animationPacer: AnimationPacer = AnimationPacer(),
) : ViewModel() {

    val recipes: MutableLiveData<List<RecipeModel>> = MutableLiveData()
    val lastSelectedRecipe: MutableLiveData<RecipeModel> = MutableLiveData()
    private var indexedRecipes = mutableMapOf<String, RecipeModel>()

    private val animationUpdateIntervalMillis: Long = 33 // 30 FPS
    private var indexedAnimations = mutableMapOf<String, AnimationProgression<Float>>()
    private val animationUpdaters = mutableMapOf<String, Subject<AnimationProgression<Float>>>()

    fun fetchRecipes() {
        api.fetchRecipes(
            { syncRecipes(it) },
            { Timber.d(it) },
        )
    }

    fun select(recipe: RecipeModel, withAnimation: AnimationProgression<Float>? = null) {
        setSelection(recipe, true)
        lastSelectedRecipe.value = indexedRecipes[recipe.id]
        withAnimation?.let { enqueueAnimation(it, recipe.id!!) }
    }

    fun deselect(recipe: RecipeModel, withAnimation: AnimationProgression<Float>? = null) {
        setSelection(recipe, false)
        withAnimation?.let { enqueueAnimation(it, recipe.id!!) }
    }

    private fun setSelection(recipe: RecipeModel, selected: Boolean) {
        indexedRecipes[recipe.id!!] = recipe.copy(selected = selected)
        recipes.value?.map { indexedRecipes[it.id]!! }
            .let { updateRecipesList(it!!) }
    }

    private fun updateRecipesList(newRecipes: List<RecipeModel>) {
//        animationUpdaters.clear()
        recipes.value = newRecipes
    }

    fun getAnimationUpdates(forRecipe: RecipeModel): Observable<AnimationProgression<Float>> {
        val key = forRecipe.id!!
        return animationUpdaters[key] ?: PublishSubject.create<AnimationProgression<Float>>().let {
            animationUpdaters[key] = it
            return it
        }
    }

    private fun enqueueAnimation(animation: AnimationProgression<Float>, withKey: String) {
        indexedAnimations[withKey] = animation
//        scheduleNextAnimationUpdate()
        updateAnimationQueue(0)
    }

    private fun updateAnimationQueue(withTimeDeltaMillis: Long) {
        val expiredAnimationKeys = mutableListOf<String>()
        indexedAnimations.forEach { (key, value) ->
            if (value.applyAndCheckCompletion(withTimeDeltaMillis)) expiredAnimationKeys.add(key)
            indexedRecipes[key]?.let { (getAnimationUpdates(it) as Subject).onNext(value) } ?: let {
                expiredAnimationKeys.add(key)
                animationUpdaters.remove(key)
            } // removing animation data for missing recipes
//            animationUpdaters[key]?.apply { onNext(value) }
        }
        expiredAnimationKeys.forEach { key -> indexedAnimations.remove(key) }
        scheduleNextAnimationUpdate()
    }

    private fun scheduleNextAnimationUpdate() {
        animationPacer.scheduleUpdate(
            animationUpdateIntervalMillis,
            this::updateAnimationQueue,
        )
    }

    private fun syncRecipes(recipes: Collection<RecipeModel>) {
        val newRecipes: List<RecipeModel> = recipes.map {
            when (val old = indexedRecipes[it.id]) {
                null -> it
                else -> it.copy(selected = old.selected)
            }
        }
        indexedRecipes = newRecipes.associateBy { it.id!! }.toMutableMap()
        updateRecipesList(newRecipes)
    }

}