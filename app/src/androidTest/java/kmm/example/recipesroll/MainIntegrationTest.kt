package kmm.example.recipesroll

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kmm.example.recipesroll.ui.RecipesListViewModel
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeoutException


@RunWith(AndroidJUnit4::class)
class MainIntegrationTest {

    @get:Rule
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testRecipeListItemLoading() {
        var activity: MainActivity
        var viewModel: RecipesListViewModel
        var numSelectionCalls = 0
        var listItemHeight = -10
        Espresso
            .onView(withId(R.id.recipes_list_view))
            .check { listView, e ->
                e?.let { throw e }

                // setup env vars
                activity = listView.context as MainActivity
                viewModel = ViewModelProvider(activity, activity.viewModelFactory)
                    .get(RecipesListViewModel::class.java)
                viewModel.lastSelectedRecipe.observeForever {
                    numSelectionCalls++
                }
                listItemHeight =
                    activity.resources.getDimension(R.dimen.recipe_item_cell_height).toInt()

                // items can't have loaded yet
                assertEquals(0, (listView as ViewGroup).childCount)
            }
            .perform(waitForListItemsLoaded(5000))

            // check list item selection
            .check { _, _ -> assertEquals(0, numSelectionCalls) }
            .perform(clickListItem(1))
            .check { listView, _ ->
                assertEquals(1, numSelectionCalls)
                assert(listItemHeight < (listView as ViewGroup).getChildAt(1).measuredHeight)
            }
            .perform(clickListItem(1))
            .check { listView, e ->
                e?.let { throw e }
                assert(listItemHeight == (listView as ViewGroup).getChildAt(1).measuredHeight)
            }
    }

    fun clickListItem(atPosition: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Simulating click on list item at position: $atPosition"
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as ViewGroup).getChildAt(atPosition).callOnClick()
            }

        }
    }

    fun waitForListItemsLoaded(timeout: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Wait for recipe list items finished loading."
            }

            override fun perform(uiController: UiController?, view: View?) {
                uiController!!.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + timeout
                do {
                    if ((view as ViewGroup).childCount > 0) return
                    uiController.loopMainThreadForAtLeast(100)
                } while (System.currentTimeMillis() < endTime)
                throw PerformException.Builder()
                    .withCause(TimeoutException())
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .build()
            }
        }
    }

}