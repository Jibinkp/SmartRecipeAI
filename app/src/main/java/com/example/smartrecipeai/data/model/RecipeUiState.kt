package com.example.smartrecipeai.data.model

import android.graphics.Bitmap

data class RecipeUiState(
    val selectedImage: Bitmap? = null,
    val ingredients: List<Ingredient> = emptyList(),
    val recipe: String = "",
    val isDetecting: Boolean = false,
    val isGeneratingRecipe: Boolean = false,
    val error: String? = null
)
