package com.bangkit.heyfo.ui.detail.recipe

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.heyfo.R
import com.bangkit.heyfo.adapter.DetailPagerAdapter
import com.bangkit.heyfo.data.response.DetailFoodResponse
import com.bangkit.heyfo.data.retrofit.ApiConfig
import com.bangkit.heyfo.data.database.Food
import com.bangkit.heyfo.databinding.ActivityDetailRecipeBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRecipeBinding
    private lateinit var pagerAdapter: DetailPagerAdapter

    private val detailViewModel: DetailRecipeViewModel by viewModels()

    private var isFavorite: Boolean = false
    private var currentFood: Food? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pagerAdapter = DetailPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Bahan"
                1 -> "Langkah"
                else -> null
            }
        }.attach()

        val uuid = intent.getStringExtra("uuid")
        if (uuid != null) {
            fetchDetailFood(uuid)
        } else {
            Toast.makeText(this, "UUID tidak tersedia", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnFavorite.setOnClickListener {
            currentFood?.let { food ->
                if (isFavorite) {
                    detailViewModel.removeFavorite(food)
                    Toast.makeText(this, "${food.name} dihapus dari favorit", Toast.LENGTH_SHORT).show()
                } else {
                    detailViewModel.addFavorite(food)
                    Toast.makeText(this, "${food.name} ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
                }
                // Ubah status favorit setelah aksi
                isFavorite = !isFavorite
                updateFavoriteButton(isFavorite)
            }
        }
    }

    private fun fetchDetailFood(uuid: String) {
        val call = ApiConfig.getApiService().getDetailFood(uuid)
        call.enqueue(object : Callback<DetailFoodResponse> {
            override fun onResponse(call: Call<DetailFoodResponse>, response: Response<DetailFoodResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { detailFoodResponse ->
                        displayFoodDetail(detailFoodResponse)
                    }
                } else {
                    Toast.makeText(this@DetailRecipeActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetailFoodResponse>, t: Throwable) {
                Toast.makeText(this@DetailRecipeActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayFoodDetail(detailFoodResponse: DetailFoodResponse) {
        detailFoodResponse.data?.let { data ->
            currentFood = Food(name = data.name ?: "", imageUrl = data.imageUrl, uuid = data.uuid)

            // Set the name
            binding.textViewName.text = data.name ?: "Nama tidak tersedia"

            // Load the image
            Glide.with(this)
                .load(data.imageUrl)
                .into(binding.imageView)

            // Update ViewPager fragments
            pagerAdapter.updateIngredients(data.ingredients?.filterNotNull() ?: emptyList())
            pagerAdapter.updateSteps(data.cookingStep?.filterNotNull() ?: emptyList())

            // Cek apakah makanan ini sudah ada di favorit
            detailViewModel.isFavorite(data.name ?: "").observe(this) { food ->
                isFavorite = food != null
                updateFavoriteButton(isFavorite)
            }
        }
    }

    private fun updateFavoriteButton(isFavorite: Boolean) {
        val drawableRes = if (isFavorite) {
            R.drawable.add_favorite // Gambar hati terisi
        } else {
            R.drawable.add_fav_border // Gambar hati kosong
        }
        binding.btnFavorite.setImageResource(drawableRes)
    }
}
