package kmm.example.recipesroll.remote

import android.annotation.SuppressLint
import com.contentful.java.cda.CDAClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kmm.example.recipesroll.BuildConfig
import kmm.example.recipesroll.model.RecipeModel


object RemoteRecipesApi : RecipesApi {

    private val client = CDAClient.builder()
        .setSpace(BuildConfig.CONTENTFUL_SPACE)
        .setToken(BuildConfig.CONTENTFUL_TOKEN)
        .build()

    @SuppressLint("CheckResult")
    override fun fetchRecipes(
        onResult: (Collection<RecipeModel>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        client
            .observeAndTransform(RecipeModel::class.java)
            .all()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(onResult, onError)
    }
}