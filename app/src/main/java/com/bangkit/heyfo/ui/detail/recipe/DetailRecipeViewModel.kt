package com.bangkit.heyfo.ui.detail.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.heyfo.data.database.Food
import com.bangkit.heyfo.data.database.FoodRepository
import kotlinx.coroutines.launch

class DetailRecipeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FoodRepository = FoodRepository(application)

    // LiveData untuk memantau status favorit berdasarkan nama
    fun isFavorite(name: String): LiveData<Food?> = repository.getFoodByName(name)

    // Fungsi untuk menambahkan ke favorit
    fun addFavorite(food: Food) {
        viewModelScope.launch {
            repository.insert(food)
        }
    }

    // Fungsi untuk menghapus dari favorit
    fun removeFavorite(food: Food) {
        viewModelScope.launch {
            repository.delete(food)
        }
    }
}

