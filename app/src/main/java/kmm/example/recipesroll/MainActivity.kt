package kmm.example.recipesroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kmm.example.recipesroll.databinding.MainActivityBinding
import kmm.example.recipesroll.remote.RecipesApi
import kmm.example.recipesroll.remote.RecipesApiFactory
import kmm.example.recipesroll.ui.RecipesListViewModel
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    val api = RecipesApiFactory().create()
    val viewModelFactory = MainActivityViewModelFactory(api)
    private lateinit var binding: MainActivityBinding
    private val backgroundImageUrl = "https://i.imgur.com/GkRPkP0.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        setContentView(R.layout.main_activity)
        binding = MainActivityBinding
            .bind(findViewById(R.id.root))
            .apply(onBindView())
    }

    private fun onBindView(): MainActivityBinding.() -> Unit = {
        val activity = this@MainActivity
        val viewModel = ViewModelProvider(activity, activity.viewModelFactory)
            .get(RecipesListViewModel::class.java)
        lifecycleOwner = activity
        recipesListView.setupListView(viewModel, activity)
        Picasso.get()
            .load(backgroundImageUrl)
            .priority(Picasso.Priority.LOW)
            .into(backgroundImage)

        viewModel.lastSelectedRecipe.observe(activity) {
            appBar.setExpanded(false, true)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class MainActivityViewModelFactory(private val api: RecipesApi) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipesListViewModel::class.java)) {
                return RecipesListViewModel(api) as T
            }
            throw IllegalArgumentException("Unknown View Model Class")
        }
    }

}