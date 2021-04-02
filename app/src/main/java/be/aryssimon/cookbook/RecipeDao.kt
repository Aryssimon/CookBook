package be.aryssimon.cookbook

import androidx.room.*

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    fun getAll(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE id LIKE :inputId")
    fun getById(inputId: Int): Recipe

    @Insert
    fun insertAll(recipe: Recipe)

    @Update
    fun updateAll(recipe: Recipe)

    @Delete
    fun delete(recipe: Recipe)
}