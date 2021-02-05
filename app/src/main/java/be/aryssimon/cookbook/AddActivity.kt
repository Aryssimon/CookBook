package be.aryssimon.cookbook

import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.graphics.fonts.FontFamily
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.room.Room

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = resources.getString(R.string.add_activity_title)
        findViewById<ImageButton>(R.id.decrease_button).isEnabled = false
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

    fun onClickAddIngredient(view: View) {
        val linearLayout = findViewById<LinearLayout>(R.id.linLayout_ingredients)
        val editText = createEditText(getString(R.string.hint_ingredients))
        linearLayout.addView(editText)
    }

    fun onClickAddSteps(view: View) {
        val linearLayout = findViewById<LinearLayout>(R.id.linLayout_steps)
        val editText = createEditText(getString(R.string.hint_steps))
        linearLayout.addView(editText)
    }

    private fun createEditText(hint: String): EditText {
        val editText = EditText(this)
        editText.setTextAppearance(R.style.fontEditText)
        editText.hint = hint
        editText.setAutofillHints(hint)
        return editText
    }

    fun onClickIncreasePeople(view: View) {
        val textView = findViewById<TextView>(R.id.insert_people)
        val value = textView.text.toString().toInt() + 1
        textView.text = value.toString()
        if (value == 2) findViewById<ImageButton>(R.id.decrease_button).isEnabled = true
    }

    fun onClickDecreasePeople(view: View) {
        val textView = findViewById<TextView>(R.id.insert_people)
        val value = textView.text.toString().toInt() - 1
        textView.text = value.toString()
        if (value == 1) findViewById<ImageButton>(R.id.decrease_button).isEnabled = false
    }

    fun onClickConfirm(view: View) {
        val prepaHour = findViewById<EditText>(R.id.edt_prepa_hour).text.toString().toInt()
        val prepaMinutes = findViewById<EditText>(R.id.edt_prepa_min).text.toString().toInt()
        val cookingHour = findViewById<EditText>(R.id.edt_cooking_hour).text.toString().toInt()
        val cookingMinutes = findViewById<EditText>(R.id.edt_cooking_min).text.toString().toInt()

        var ingredients = ""
        val ingredientsLayout = findViewById<LinearLayout>(R.id.linLayout_ingredients)
        for (i in 0 until ingredientsLayout.childCount) {
            ingredients += ingredientsLayout.getChildAt(i).toString() + "\n"
        }
        ingredients = ingredients.substring(0 until (ingredients.length - 2)) // remove last \n

        var steps = ""
        val stepsLayout = findViewById<LinearLayout>(R.id.linLayout_steps)
        for (i in 0 until stepsLayout.childCount) {
            steps += stepsLayout.getChildAt(i).toString() + "\n"
        }
        steps = steps.substring(0 until (steps.length - 2)) // remove last \n

        val newRecipe = Recipe(
            title = findViewById<EditText>(R.id.insert_title).text.toString(),
            ingredients = ingredients,
            steps = steps,
            totalTime = ((prepaHour + cookingHour) * 60) + prepaMinutes + cookingMinutes,
            preparationTime = (prepaHour * 60) + prepaMinutes,
            cookingTime = (cookingHour * 60) + cookingMinutes,
            people = findViewById<TextView>(R.id.insert_people).text.toString().toInt(),
            price = findViewById<RatingBar>(R.id.ratingBar_price).rating.toString().toFloat().toInt()
        )

        val db = Room.databaseBuilder(this, AppDatabase::class.java, "database-name")
            .allowMainThreadQueries()
            .build()
        val recipeDao = db.recipeDao()
        recipeDao.insertAll(newRecipe)
    }
}