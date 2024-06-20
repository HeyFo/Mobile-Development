package com.bangkit.heyfo.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.heyfo.adapter.FavoriteFoodAdapter
import com.bangkit.heyfo.data.database.Food
import com.bangkit.heyfo.data.response.DataItem
import com.bangkit.heyfo.databinding.FragmentFavoriteBinding
import com.bangkit.heyfo.ui.detail.recipe.DetailRecipeActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteFoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Inisialisasi adapter
        adapter = FavoriteFoodAdapter { food ->
            onFoodItemClicked(food.uuid ?: "")
        }

        // Set up RecyclerView
        binding.rvFavorites.layoutManager = LinearLayoutManager(context)
        binding.rvFavorites.adapter = adapter // Set adapter to RecyclerView

        // Show loading initially
        binding.progressBar.visibility = View.VISIBLE
        binding.rvFavorites.visibility = View.GONE

        // Observe data
        favoriteViewModel.allFoods.observe(viewLifecycleOwner, Observer { foods ->
            if (foods != null && foods.isNotEmpty()) {
                adapter.submitList(foods)
                binding.rvFavorites.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else {
                // Handle empty state
                binding.rvFavorites.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                // Optionally, show a message or image indicating empty state
            }
        })
    }

    private fun onFoodItemClicked(uuid: String) {
        val intent = Intent(requireContext(), DetailRecipeActivity::class.java).apply {
            putExtra("uuid", uuid)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
