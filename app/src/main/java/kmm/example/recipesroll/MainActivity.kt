package kmm.example.recipesroll

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kmm.example.recipesroll.databinding.MainActivityBinding
import kmm.example.recipesroll.remote.DummyRecipesApi
import kmm.example.recipesroll.remote.RecipesApi
import kmm.example.recipesroll.ui.RecipesListView
import kmm.example.recipesroll.ui.RecipesViewModel
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModelFactory: MainActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        setContentView(R.layout.main_activity)
        viewModelFactory = MainActivityViewModelFactory(DummyRecipesApi)
        binding = MainActivityBinding.bind(findViewById(R.id.root)).apply {
            val owner = this@MainActivity
            viewModel = ViewModelProvider(owner, viewModelFactory)
                .get(RecipesViewModel::class.java)
            lifecycleOwner = owner

            val adapter = RecipesListView.RecipesListAdapter()
            recipesListView.adapter = adapter
            recipesListView.layoutManager = LinearLayoutManager(owner)

            viewModel?.getRecipes()?.observe(owner, { adapter.setRecipes(it) })
            viewModel?.updateRecipes()
        }
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