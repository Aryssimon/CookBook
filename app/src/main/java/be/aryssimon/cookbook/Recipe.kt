package be.aryssimon.cookbook

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String,
    var ingredients: String,
    var steps: String,
    var people: Int,
    var price: Int,
    // Time is in minutes
    var totalTime: Int,
    var preparationTime: Int,
    var cookingTime: Int
)