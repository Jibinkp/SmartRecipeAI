package com.example.smartrecipeai.data.repository

import android.graphics.Bitmap
import com.example.smartrecipeai.BuildConfig
import com.example.smartrecipeai.data.detector.FoodObjectDetector
import com.example.smartrecipeai.data.model.Content
import com.example.smartrecipeai.data.model.GeminiRequest
import com.example.smartrecipeai.data.model.Ingredient
import com.example.smartrecipeai.data.model.Part
import com.example.smartrecipeai.data.remote.GeminiApi
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipeRepository(
    private val foodObjectDetector: FoodObjectDetector,
    private val geminiApi: GeminiApi
) {

    suspend fun detectIngredients(
        bitmap: Bitmap
    ): List<Ingredient> {

        val detectedObjects = foodObjectDetector.detect(bitmap)

        return detectedObjects
            .groupBy { it.name.lowercase() }
            .map { (_, objects) ->
                val best = objects.maxBy { it.confidence }

                Ingredient(
                    name = best.name,
                    confidence = best.confidence
                )
            }
    }

    suspend fun generateRecipe(
        ingredients: List<Ingredient>
    ): String {

        val ingredientNames = ingredients
            .joinToString(", ") { it.name }

        val prompt = """
        You are a professional chef.

        Generate recipes using these ingredients:
        $ingredientNames

        Include:
        1. Recipe name
        2. Ingredients with quantities
        3. Cooking instructions
        4. Preparation time
        5. Cooking time
        6. Difficulty level
    """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt)
                    )
                )
            )
        )

        val response = geminiApi.generateRecipe(
            apiKey = BuildConfig.GEMINI_API_KEY,
            request = request
        )

        return response.candidates
            ?.firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
            ?.text ?: "No recipe generated"
    }
}