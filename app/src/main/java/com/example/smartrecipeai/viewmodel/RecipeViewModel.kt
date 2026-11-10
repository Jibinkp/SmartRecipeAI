package com.example.smartrecipeai.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartrecipeai.data.classifier.FoodClassifier
import com.example.smartrecipeai.data.model.Ingredient
import com.example.smartrecipeai.data.model.RecipeUiState
import com.example.smartrecipeai.data.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    var uiState by mutableStateOf(RecipeUiState())
        private set

    fun detectIngredients(bitmap: Bitmap) {
        viewModelScope.launch {

            uiState = uiState.copy(
                selectedImage = bitmap,
                isDetecting = true,
                ingredients = emptyList(),
                recipe = "",
                error = null
            )

            try {
                val ingredients =
                    repository.detectIngredients(bitmap)

                uiState = uiState.copy(
                    ingredients = ingredients,
                    isDetecting = false,
                    error = if (ingredients.isEmpty()) {
                        "No ingredients detected. Try another image."
                    } else null
                )

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isDetecting = false,
                    error = e.message ?: "Detection failed"
                )
            }
        }
    }

    fun generateRecipe() {
        val ingredients = uiState.ingredients

        if (ingredients.isEmpty()) return

        viewModelScope.launch {

            uiState = uiState.copy(
                isGeneratingRecipe = true,
                error = null
            )

            try {
                val recipe =
                    repository.generateRecipe(ingredients)

                uiState = uiState.copy(
                    recipe = recipe,
                    isGeneratingRecipe = false
                )

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isGeneratingRecipe = false,
                    error = e.message ?: "Recipe generation failed"
                )
            }
        }
    }
}