package com.bangkit.heyfo.ui.home

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.heyfo.R
import com.bangkit.heyfo.adapter.ArticleAdapter
import com.bangkit.heyfo.adapter.FoodAdapter
import com.bangkit.heyfo.data.response.DataItem
import com.bangkit.heyfo.data.response.DataItemArticle
import com.bangkit.heyfo.databinding.FragmentHomeBinding
import com.bangkit.heyfo.ui.detail.article.DetailArticleActivity
import com.bangkit.heyfo.ui.detail.recipe.DetailRecipeActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var articleAdapter: ArticleAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private var foodList: List<DataItem> = listOf()
    private var articleList: List<DataItemArticle> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setup RecyclerView for food items
        val recyclerView = binding.rvFood
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val space = resources.getDimensionPixelSize(R.dimen.item_space)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.right = space
            }
        })
        foodAdapter = FoodAdapter(emptyList()) { food ->
            onFoodItemClicked(food)
        }
        recyclerView.adapter = foodAdapter

        // Setup RecyclerView for articles
        val articleRecyclerView = binding.rvArticle
        articleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        articleAdapter = ArticleAdapter(emptyList()) { article ->
            onArticleItemClicked(article)
        }
        articleRecyclerView.adapter = articleAdapter

        // Fetch data from ViewModel
        homeViewModel.fetchData()
        homeViewModel.fetchArticleData()

        // Observe LiveData for food list
        homeViewModel.foodList.observe(viewLifecycleOwner) { foods ->
            foodList = foods
            foodAdapter.updateData(foods)
        }

        // Observe LiveData for article list
        homeViewModel.articleList.observe(viewLifecycleOwner) { articles ->
            articles?.let {
                articleList = it
                articleAdapter.updateData(it)
            }
        }

        // Observe LiveData for loading state
        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe LiveData for error message
        homeViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                homeViewModel.clearError()
            }
        }

        // Setup search functionality
        setupSearchFunctionality()

        return root
    }

    private fun setupSearchFunctionality() {
        binding.btnSearch.setOnClickListener {
            val searchText = binding.etSearch.text.toString()
            filterFoodList(searchText)
        }
    }

    private fun filterFoodList(query: String) {
        val filteredList = foodList.filter { food ->
            food.name?.contains(query, ignoreCase = true) == true
        }
        foodAdapter.updateData(filteredList)
    }

    private fun onFoodItemClicked(food: DataItem) {
        val intent = Intent(requireContext(), DetailRecipeActivity::class.java).apply {
            putExtra("uuid", food.uuid)
        }
        startActivity(intent)
    }

    private fun onArticleItemClicked(article: DataItemArticle) {
        val intent = Intent(requireContext(), DetailArticleActivity::class.java).apply {
            putExtra("article_uuid", article.uuid)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
