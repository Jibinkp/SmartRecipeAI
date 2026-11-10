
package com.example.smartrecipeai.data.detector

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector

data class DetectedObject(
    val name: String,
    val confidence: Float,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)

class FoodObjectDetector(context: Context) {

    private val detector: ObjectDetector

    init {
        val options = ObjectDetector.ObjectDetectorOptions.builder()
            .setMaxResults(20)
            .setScoreThreshold(0.35f)
            .build()

        detector = ObjectDetector.createFromFileAndOptions(
            context.applicationContext,
            "food_detector.tflite",
            options
        )
    }

    suspend fun detect(bitmap: Bitmap): List<DetectedObject> =
        withContext(Dispatchers.IO) {

            val tensorImage = TensorImage.fromBitmap(bitmap)
            val results = detector.detect(tensorImage)

            results.mapNotNull { detection ->

                val category = detection.categories.firstOrNull()
                    ?: return@mapNotNull null

                val box = detection.boundingBox

                DetectedObject(
                    name = category.label,
                    confidence = category.score,
                    left = box.left,
                    top = box.top,
                    right = box.right,
                    bottom = box.bottom
                )
            }
        }

    fun close() {
        detector.close()
    }
}
