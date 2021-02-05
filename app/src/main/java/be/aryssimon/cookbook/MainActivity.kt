package be.aryssimon.cookbook

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var recipeDao: RecipeDao
    private lateinit var recipeList: List<Recipe>
    private var index: Int = -1

    private var recipeTitle: TextView? = null
    private var recipeTotalTime: TextView? = null
    private var recipePeople: TextView? = null
    private var recipePrice: TextView? = null
    private var recipeIngredients: TextView? = null
    private var recipeSteps: TextView? = null
    private var recipePreparationTime: TextView? = null
    private var recipeCookingTime: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
        setAddFloatingActionButton()
        initializeVariables()
        disableUselessButtons()
        showRecipe()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        handleSearch(menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(this@MainActivity, R.string.not_implemented_yet, Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_search -> {
                true
            }
            R.id.action_delete -> {
                if (recipeList.isNotEmpty()) {
                    deleteRecipe()
                } else {
                    Toast.makeText(this@MainActivity, R.string.nothing_to_delete, Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setAddFloatingActionButton() {
        findViewById<FloatingActionButton>(R.id.add_recipe).setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleSearch(menu: Menu) {
        val searchView = menu.findItem(R.id.action_search).actionView as android.widget.SearchView
        searchView.queryHint = resources.getString(R.string.search_bar_hint)
        val width = getScreenWidth()
        val pxToSave = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            120f,
            resources.displayMetrics
        ).toInt()
        searchView.maxWidth = width - pxToSave
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                val foundIndex = recipeList.indexOfFirst {
                    query.toLowerCase(Locale.ROOT) in it.title.toLowerCase(Locale.ROOT)
                }
                if (foundIndex > -1) {
                    index = foundIndex
                    showRecipe()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.search_no_result),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    fun onClickNext(view: View) {
        index += 1
        if (index >= recipeList.size) index = 0
        showRecipe()

    }

    fun onClickPrevious(view: View) {
        index -= 1
        if (index < 0) index = recipeList.size - 1
        showRecipe()
    }

    @SuppressLint("SetTextI18n")
    private fun showRecipe() {
        if (recipeList.isNotEmpty()) {
            val currentRecipe = recipeList[index]

            var priceRating = ""
            for (i in 0 until currentRecipe.price) priceRating += getString(R.string.text_price)

            recipeTitle?.text = currentRecipe.title
            recipePeople?.text =
                currentRecipe.people.toString() + " " + getString(R.string.text_people)
            recipePrice?.text = priceRating
            recipeIngredients?.text = currentRecipe.ingredients
            recipeSteps?.text = currentRecipe.steps
            recipeTotalTime?.text = minutesToTimeFormat(currentRecipe.totalTime)
            recipePreparationTime?.text =
                getString(R.string.text_prepa_time) + " " + minutesToTimeFormat(currentRecipe.preparationTime)
            recipeCookingTime?.text =
                getString(R.string.text_cooking_time) + " " + minutesToTimeFormat(currentRecipe.cookingTime)
        }
    }

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics) // !! Deprecated
        return displayMetrics.widthPixels
    }

    private fun disableUselessButtons() {
        if (recipeList.size < 2) {
            val next = findViewById<Button>(R.id.nextButton)
            next.isEnabled = false
            next.setBackgroundColor(getColor(R.color.light_gray))
            val previous = findViewById<Button>(R.id.previousButton)
            previous.isEnabled = false
            previous.setBackgroundColor(getColor(R.color.light_gray))
        }
    }

    private fun initializeVariables() {
        recipeDao = Room
            .databaseBuilder(this.applicationContext, AppDatabase::class.java, "database-name")
            .allowMainThreadQueries()
            .build()
            .recipeDao()
        recipeList = recipeDao.getAll()
        index = recipeList.size / 2
        recipeTitle = findViewById(R.id.recipe_title)
        recipeTotalTime = findViewById(R.id.recipe_total_time)
        recipePeople = findViewById(R.id.recipe_people)
        recipePrice = findViewById(R.id.recipe_price)
        recipeIngredients = findViewById(R.id.recipe_list_ingredients)
        recipeSteps = findViewById(R.id.recipe_list_steps)
        recipePreparationTime = findViewById(R.id.recipe_prepa_time)
        recipeCookingTime = findViewById(R.id.recipe_cooking_time)
    }

    private fun minutesToTimeFormat(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        if (remainingMinutes < 10) return "${hours}h0${remainingMinutes}min"
        return "${hours}h${remainingMinutes}min"
    }

    private fun deleteRecipe() {
        val currentRecipe = recipeList[index]
        recipeDao.delete(currentRecipe)
        recipeList = recipeDao.getAll()
        if (recipeList.isEmpty()) {
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        } else {
            index -= 1
            if (index < 0) index = recipeList.size - 1
            showRecipe()
        }
    }
}