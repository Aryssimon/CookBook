package be.aryssimon.cookbook

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    fun getAll(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE id LIKE :inputId")
    fun getById(inputId: Int): Recipe

    @Insert
    fun insertAll(vararg recipe: Recipe)

    @Delete
    fun delete(recipe: Recipe)
}