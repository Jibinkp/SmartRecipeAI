package com.example.smartrecipeai.data.remote

import com.example.smartrecipeai.data.model.GeminiRequest
import com.example.smartrecipeai.data.model.GeminiResponse
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GeminiApi {

    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    @Headers("Content-Type: application/json")
    suspend fun generateRecipe(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}