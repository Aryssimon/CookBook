package be.aryssimon.cookbook

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = resources.getString(R.string.edit_activity_title)
        fillWithRecipeInfos()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun fillWithRecipeInfos() {
        val intentExtras = intent.extras
        if (intentExtras != null) {
            val recipeDao = Room
                    .databaseBuilder(this.applicationContext, AppDatabase::class.java, "database-name")
                    .allowMainThreadQueries()
                    .build()
                    .recipeDao()
            val recipeList = recipeDao.getAll()
            val currentRecipe = recipeList[intentExtras.getInt("recipeIndex")]

            loadTitle(currentRecipe)
            loadIngredients(currentRecipe)
            loadSteps(currentRecipe)
            loadPeoples(currentRecipe)
            loadPrice(currentRecipe)
            loadPreparationTime(currentRecipe)
            loadCookingTime(currentRecipe)
            
        } else {
            error(resources.getString(R.string.edit_error_launching))
        }
    }

    private fun addOneIngredient(text: String) {
        val linearLayout = findViewById<LinearLayout>(R.id.linLayout_ingredients)
        val editText = createEditText(getString(R.string.hint_ingredients))
        editText.setText(text)
        linearLayout.addView(editText)
    }

    private fun addOneStep(text: String) {
        val linearLayout = findViewById<LinearLayout>(R.id.linLayout_steps)
        val editText = createEditText(getString(R.string.hint_steps))
        editText.setText(text)
        linearLayout.addView(editText)
    }

    private fun createEditText(hint: String): EditText {
        val editText = EditText(this)
        editText.setTextAppearance(R.style.fontEditText)
        editText.hint = hint
        editText.setAutofillHints(hint)
        return editText
    }

    private fun loadTitle(currentRecipe: Recipe) {
        val recipeTitle = findViewById<EditText>(R.id.insert_title)
        recipeTitle.setText(currentRecipe.title)
    }

    private fun loadIngredients(currentRecipe: Recipe) {
        val splitIngredients = currentRecipe.ingredients.split("\n")
        for (line in splitIngredients) {
            addOneIngredient(line.subSequence(1, line.length) as String)
        }
    }

    private fun loadSteps(currentRecipe: Recipe) {
        val splitSteps = currentRecipe.steps.split("\n")
        for (line in splitSteps) {
            addOneStep(line.subSequence(1, line.length) as String)
        }
    }

    private fun loadPeoples(currentRecipe: Recipe) {
        val peopleTextView = findViewById<TextView>(R.id.insert_people)
        val peoples = currentRecipe.people
        peopleTextView.text = peoples.toString()
        if (peoples == 1) findViewById<ImageButton>(R.id.decrease_button).isEnabled = false
    }

    private fun loadPrice(currentRecipe: Recipe) {
        val priceRatingBar = findViewById<RatingBar>(R.id.ratingBar_price)
        val price = currentRecipe.price
        priceRatingBar.rating = price.toFloat()
    }

    private fun loadPreparationTime(currentRecipe: Recipe) {
        val preparationTimeHours = findViewById<EditText>(R.id.edt_prepa_hour)
        val preparationTimeMinutes = findViewById<EditText>(R.id.edt_prepa_min)
        val hours = currentRecipe.preparationTime / 60
        val minutes = currentRecipe.preparationTime % 60
        preparationTimeHours.setText(hours.toString())
        preparationTimeMinutes.setText(minutes.toString())
    }

    private fun loadCookingTime(currentRecipe: Recipe) {
        val preparationTimeHours = findViewById<EditText>(R.id.edt_cooking_hour)
        val preparationTimeMinutes = findViewById<EditText>(R.id.edt_cooking_min)
        val hours = currentRecipe.cookingTime / 60
        val minutes = currentRecipe.cookingTime % 60
        preparationTimeHours.setText(hours.toString())
        preparationTimeMinutes.setText(minutes.toString())
    }


}