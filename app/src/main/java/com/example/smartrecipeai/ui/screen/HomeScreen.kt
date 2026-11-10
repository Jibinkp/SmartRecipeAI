
package com.example.smartrecipeai.ui.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smartrecipeai.data.detector.DetectedObject
import com.example.smartrecipeai.viewmodel.RecipeViewModel

@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    viewModel: RecipeViewModel
) {
    val context = LocalContext.current
    val state = viewModel.uiState

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

        uri?.let {
            try {
                val bitmap = context.contentResolver
                    .openInputStream(it)
                    ?.use { stream ->
                        BitmapFactory.decodeStream(stream)
                    }

                bitmap?.let(viewModel::detectIngredients)

            } catch (e: Exception) {
                // Handle image loading errors in your UI.
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                text = "Smart Recipe AI",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Button(
                onClick = {
                    imagePicker.launch("image/*")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Fruits / Vegetables")
            }
        }

        item {
            state.selectedImage?.let { bitmap ->

                DetectionImage(
                    bitmap = bitmap,
                    detections = state.ingredients.map {
                        DetectedObject(
                            name = it.name,
                            confidence = it.confidence,
                            left = 0f,
                            top = 0f,
                            right = 0f,
                            bottom = 0f
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            if (state.isDetecting) {
                CircularProgressIndicator()
                Text("Detecting ingredients...")
            }
        }

        item {
            Text(
                "Detected Ingredients",
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(state.ingredients) { ingredient ->
            Text(
                "${ingredient.name} - " +
                        "${(ingredient.confidence * 100).toInt()}%"
            )
        }

        item {
            Button(
                onClick = viewModel::generateRecipe,
                enabled = state.ingredients.isNotEmpty() &&
                        !state.isGeneratingRecipe,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generate Recipe")
            }
        }

        item {
            if (state.isGeneratingRecipe) {
                CircularProgressIndicator()
            }
        }

        item {
            if (state.recipe.isNotBlank()) {
                Text(
                    text = state.recipe,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        item {
            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun DetectionImage(
    bitmap: Bitmap,
    detections: List<DetectedObject>,
    modifier: Modifier = Modifier
) {
    // Bounding box drawing is shown in the next section.
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Selected food image",
        modifier = modifier
            .aspectRatio(
                bitmap.width.toFloat() / bitmap.height
            ),
        contentScale = ContentScale.FillBounds
    )
}
