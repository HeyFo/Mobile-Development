package com.bangkit.heyfo.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(food: Food)

    @Update
    fun update(food: Food)

    @Delete
    fun delete(food: Food)

    @Query("SELECT * FROM food")
    fun getAllFoods(): LiveData<List<Food>>

    @Query("SELECT * FROM food WHERE name = :name LIMIT 1")
    fun getFoodByName(name: String): LiveData<Food?>
}
