package com.bangkit.heyfo.data.database

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FoodRepository(application: Application) {
    private val mFoodDao: FoodDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FoodRoomDatabase.getDatabase(application)
        mFoodDao = db.foodDao()
    }

    fun getAllFoods(): LiveData<List<Food>> = mFoodDao.getAllFoods()

    fun getFoodByName(name: String): LiveData<Food?> = mFoodDao.getFoodByName(name)

    fun insert(food: Food) {
        executorService.execute { mFoodDao.insert(food) }
    }

    fun delete(food: Food) {
        executorService.execute { mFoodDao.delete(food) }
    }

    fun update(food: Food) {
        executorService.execute { mFoodDao.update(food) }
    }
}
