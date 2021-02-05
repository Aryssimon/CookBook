package be.aryssimon.cookbook

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
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

class MainActivity : AppCompatActivity() {

    private lateinit var recipeList: List<Recipe>
    private var index: Int? = null

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
                Toast.makeText(this@MainActivity, R.string.not_implemented_yet, Toast.LENGTH_SHORT)
                    .show()
                true
            }
            R.id.action_search -> {
                true
            }
            R.id.action_delete -> {
                Toast.makeText(this@MainActivity, R.string.not_implemented_yet, Toast.LENGTH_SHORT)
                    .show()
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

    private fun getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics) // !! Deprecated
        return displayMetrics.widthPixels
    }

    private fun handleSearch(menu: Menu) {
        val searchView = menu.findItem(R.id.action_search).actionView as android.widget.SearchView
        val width = getScreenWidth()
        searchView.queryHint = this.resources.getString(R.string.search_bar_hint)
        searchView.maxWidth = width - ((width / 100) * 35) //set width to 65% of the screen
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun showRecipe() {
        if (recipeList.isNotEmpty()) {
            val currentRecipe = recipeList[index?:0]

            var priceRating = ""
            for (i in 0 until currentRecipe.price) priceRating += getString(R.string.text_price)

            recipeTitle?.text = currentRecipe.title
            recipePeople?.text = currentRecipe.people.toString() + " " + getString(R.string.text_people)
            recipePrice?.text = priceRating
            recipeIngredients?.text = currentRecipe.ingredients
            recipeSteps?.text = currentRecipe.steps
            recipeTotalTime?.text = minutesToTimeFormat(currentRecipe.totalTime)
            recipePreparationTime?.text = getString(R.string.text_prepa_time) + " " + minutesToTimeFormat(currentRecipe.preparationTime)
            recipeCookingTime?.text = getString(R.string.text_cooking_time) + " " + minutesToTimeFormat(currentRecipe.cookingTime)
        }
    }

    private fun disableUselessButtons() {
        if (recipeList.size < 2) {
            findViewById<Button>(R.id.nextButton).isEnabled = false
            findViewById<Button>(R.id.previousButton).isEnabled = false
        }
    }

    fun onClickNext(view: View) {
        index = (index?:0) + 1
        if (index!! >= recipeList.size) {
            index = 0
            showRecipe()
        }

    }

    fun onClickPrevious(view: View) {
        index = (index?:0) - 1
        if (index!! < 0) {
            index = recipeList.size - 1
            showRecipe()
        }
    }

    private fun initializeVariables() {
        recipeList = Room
            .databaseBuilder(this.applicationContext, AppDatabase::class.java, "database-name")
            .allowMainThreadQueries()
            .build()
            .recipeDao()
            .getAll()
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
        return "${hours}h${remainingMinutes}min"
    }
}