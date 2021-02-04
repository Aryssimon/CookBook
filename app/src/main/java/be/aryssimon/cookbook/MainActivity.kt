package be.aryssimon.cookbook

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title
        setAddFloatingActionButton()

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
}