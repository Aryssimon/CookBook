package be.aryssimon.cookbook

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room

class AddActivity : AppCompatActivity() {

    private var recipeTitle: String = ""
    private var recipeIngredients: String = ""
    private var recipeSteps: String = ""
    private var recipePrepaTime: Int = 0
    private var recipeCookingTime: Int = 0
    private var recipePrice: Int = 0
    private var recipePeople: Int = 0


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
        initializeVariables()
        if (checkInput()) {
            val newRecipe = Recipe(
                title = recipeTitle,
                ingredients = recipeIngredients,
                steps = recipeSteps,
                totalTime = recipePrepaTime + recipeCookingTime,
                preparationTime = recipePrepaTime,
                cookingTime = recipeCookingTime,
                people = recipePeople,
                price = recipePrice
            )

            val recipeDao = Room.databaseBuilder(this, AppDatabase::class.java, "database-name")
                .allowMainThreadQueries()
                .build()
                .recipeDao()
            recipeDao.insertAll(newRecipe)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkInput(): Boolean {
        return when {
            recipeTitle.isEmpty() -> {
                Toast.makeText(
                    this@AddActivity,
                    getString(R.string.empty_title),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            recipeIngredients.isEmpty() -> {
                Toast.makeText(
                    this@AddActivity,
                    getString(R.string.empty_ingredients),
                    Toast.LENGTH_SHORT
                ).show()

                false
            }
            recipeSteps.isEmpty() -> {
                Toast.makeText(
                    this@AddActivity,
                    getString(R.string.empty_steps),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> true

        }
    }

    private fun initializeVariables() {
        recipeTitle = findViewById<EditText>(R.id.insert_title).text.toString()

        val ingredientsLayout = findViewById<LinearLayout>(R.id.linLayout_ingredients)
        recipeIngredients = ""
        for (i in 0 until ingredientsLayout.childCount) {
            val line = (ingredientsLayout.getChildAt(i) as EditText).text.toString()
            if (line.isNotEmpty()) {
                recipeIngredients += "• $line"
                if (i < ingredientsLayout.childCount - 1) recipeIngredients += "\n"
            }

        }

        val stepsLayout = findViewById<LinearLayout>(R.id.linLayout_steps)
        recipeSteps = ""
        for (i in 0 until stepsLayout.childCount) {
            val line = (stepsLayout.getChildAt(i) as EditText).text.toString()
            if (line.isNotEmpty()) {
                recipeSteps += "• $line"
                if (i < stepsLayout.childCount - 1) recipeSteps += "\n"
            }

        }

        recipePeople = findViewById<TextView>(R.id.insert_people).text.toString().toInt()

        recipePrice =
            findViewById<RatingBar>(R.id.ratingBar_price).rating.toString().toFloat().toInt()

        val prepaHoursString = findViewById<EditText>(R.id.edt_prepa_hour).text.toString()
        val prepaHours = if (prepaHoursString.isNotEmpty()) prepaHoursString.toInt() else 0
        val prepaMinutesString = findViewById<EditText>(R.id.edt_prepa_min).text.toString()
        val prepaMinutes = if (prepaMinutesString.isNotEmpty()) prepaMinutesString.toInt() else 0
        recipePrepaTime = (prepaHours * 60) + prepaMinutes

        val cookingHoursString = findViewById<EditText>(R.id.edt_cooking_hour).text.toString()
        val cookingHours = if (cookingHoursString.isNotEmpty()) cookingHoursString.toInt() else 0
        val cookingMinutesString = findViewById<EditText>(R.id.edt_cooking_min).text.toString()
        val cookingMinutes =
            if (cookingMinutesString.isNotEmpty()) cookingMinutesString.toInt() else 0
        recipeCookingTime = (cookingHours * 60) + cookingMinutes

    }
}