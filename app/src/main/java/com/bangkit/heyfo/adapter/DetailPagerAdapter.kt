package com.bangkit.heyfo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bangkit.heyfo.ui.detail.recipe.IngredientsFragment
import com.bangkit.heyfo.ui.detail.recipe.StepsFragment

class DetailPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private var ingredientsFragment: IngredientsFragment? = null
    private var stepsFragment: StepsFragment? = null

    private var ingredients: List<String> = emptyList()
    private var steps: List<String> = emptyList()

    fun updateIngredients(newIngredients: List<String>) {
        ingredients = newIngredients
        ingredientsFragment?.setIngredients(ingredients)
    }

    fun updateSteps(newSteps: List<String>) {
        steps = newSteps
        stepsFragment?.setSteps(steps)
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ingredientsFragment = IngredientsFragment().apply {
                    setIngredients(ingredients)
                }
                ingredientsFragment!!
            }
            1 -> {
                stepsFragment = StepsFragment().apply {
                    setSteps(steps)
                }
                stepsFragment!!
            }
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}
