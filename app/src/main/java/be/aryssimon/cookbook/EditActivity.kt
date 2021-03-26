package be.aryssimon.cookbook

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = resources.getString(R.string.edit_activity_title)


        val b = intent.extras
        if (b != null) {
            fillWithRecipeInfos(b.getInt("recipeIndex"))
        } else {
            error(resources.getString(R.string.edit_error_launching))
        }


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

    private fun fillWithRecipeInfos(recipeIndex: Int) {
        val recipeDao = Room
            .databaseBuilder(this.applicationContext, AppDatabase::class.java, "database-name")
            .allowMainThreadQueries()
            .build()
            .recipeDao()
        val recipeList = recipeDao.getAll()
        val currentRecipe = recipeList[recipeIndex]

        val recipeTitle = findViewById<EditText>(R.id.insert_title)
        recipeTitle.setText(currentRecipe.title)

        val splitIngredients = currentRecipe.ingredients.split("\n")
        for (line in splitIngredients) {
            addOneIngredient(line)
        }

        val splitSteps = currentRecipe.steps.split("\n")
        for (line in splitSteps) {
            addOneStep(line)
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
}