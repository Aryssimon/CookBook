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
        // Add recipe to DB
    }
}