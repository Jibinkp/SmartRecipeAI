package com.example.smartrecipeai.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartrecipeai.data.detector.FoodObjectDetector
import com.example.smartrecipeai.data.remote.RetrofitInstance
import com.example.smartrecipeai.data.repository.RecipeRepository

class RecipeViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {

            val detector = FoodObjectDetector(application)

            val repository = RecipeRepository(
                foodObjectDetector = detector,
                geminiApi = RetrofitInstance.geminiApi
            )

            return RecipeViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}