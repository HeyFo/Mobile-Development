package com.bangkit.heyfo.ui.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.heyfo.SharedPreferences
import com.bangkit.heyfo.adapter.PredictAdapter
import com.bangkit.heyfo.data.response.DataItemPredict
import com.bangkit.heyfo.databinding.ActivityListBinding
import com.bangkit.heyfo.ui.detail.recipe.DetailRecipeActivity

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var adapter: PredictAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = SharedPreferences()
        val predictList = sharedPreferences.loadPredictList(this)

        setupRecyclerView(predictList)
    }

    private fun setupRecyclerView(predictList: List<DataItemPredict>) {
        adapter = PredictAdapter(predictList) { uuid ->
            // Handle item click here
            val intent = Intent(this, DetailRecipeActivity::class.java)
            intent.putExtra("uuid", uuid)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListActivity)
            adapter = this@ListActivity.adapter
        }
    }
}
