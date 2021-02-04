package be.aryssimon.cookbook

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true) var id : Int,
    var title: String,
    var ingredients: String,
    var steps: String,
    var totalTime: String,
    var preparationTime: String,
    var cookingTime: String,
    var people: String,
    var price: String
)