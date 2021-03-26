package be.aryssimon.cookbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EditActivity : AddActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        title = resources.getString(R.string.edit_activity_title)
        

    }
}