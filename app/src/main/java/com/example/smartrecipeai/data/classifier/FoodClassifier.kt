package com.example.smartrecipeai.data.classifier

import android.content.Context
import android.graphics.Bitmap
import com.example.smartrecipeai.data.model.Ingredient
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class FoodClassifier(context: Context) {
    private val classifier: ImageClassifier =
        ImageClassifier.createFromFileAndOptions(
            context,
            "food_vegetable_fruit.tflite",
            ImageClassifier.ImageClassifierOptions.builder()
                .setMaxResults(10)
                .setScoreThreshold(0.15f)
                .build()
        )

    fun detect(bitmap: Bitmap): List<Ingredient> {

        val tensorImage = TensorImage.fromBitmap(bitmap)

        val results = classifier.classify(tensorImage)

        return results
            .flatMap { it.categories }
            .filter { it.score >= 0.15f }
            .distinctBy { it.label.lowercase() }
            .sortedByDescending { it.score }
            .map {
                Ingredient(
                    name = it.label,
                    confidence = it.score
                )
            }
    }
}