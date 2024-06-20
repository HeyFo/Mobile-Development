package com.bangkit.heyfo.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.heyfo.data.database.Food
import com.bangkit.heyfo.data.database.FoodRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FoodRepository = FoodRepository(application)
    val allFoods: LiveData<List<Food>> = repository.getAllFoods()

    fun insert(food: Food) = viewModelScope.launch {
        repository.insert(food)
    }

    fun delete(food: Food) = viewModelScope.launch {
        repository.delete(food)
    }

    fun update(food: Food) = viewModelScope.launch {
        repository.update(food)
    }
}
