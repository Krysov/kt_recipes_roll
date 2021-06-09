package kmm.example.recipesroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import kmm.example.recipesroll.databinding.MainActivityBinding
import kmm.example.recipesroll.remote.RecipesApiFactory
import kmm.example.recipesroll.remote.RecipesApi
import kmm.example.recipesroll.ui.RecipesViewModel
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    val api = RecipesApiFactory().create()
    val viewModelFactory = MainActivityViewModelFactory(api)
    private lateinit var binding: MainActivityBinding

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
        viewModel = ViewModelProvider(activity, activity.viewModelFactory)
            .get(RecipesViewModel::class.java)
        lifecycleOwner = activity
        recipesListView.setupListView(viewModel!!, activity)
        Picasso.get()
            .load("https://i.imgur.com/GkRPkP0.jpg")
            .priority(Picasso.Priority.LOW)
            .into(backgroundImage)
    }

    @Suppress("UNCHECKED_CAST")
    class MainActivityViewModelFactory(private val api: RecipesApi) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipesViewModel::class.java)) {
                return RecipesViewModel(api) as T
            }
            throw IllegalArgumentException("Unknown View Model Class")
        }
    }

}