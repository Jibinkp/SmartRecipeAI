package com.example.smartrecipeai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RecipeScreen(innerPadding)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            RecipeScreen(innerPadding)
        }
    }
}

@Composable
private fun RecipeScreen(innerPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Smart Recipe AI", style = MaterialTheme.typography.headlineMedium)
            Text("Scan your produce and discover what you can cook.")
        }
        item {
            Button(onClick = {}) {
                Text("Choose fruit / vegetable photo")
            }
        }

        val ingredients = listOf("Apple", "Banana", "Carrot")
        item { Text("Detected ingredients", style = MaterialTheme.typography.titleLarge) }
        items(ingredients) { ingredient ->
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text("${ingredient}")
                IconButton(onClick = {  }) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }
        }
        item {
            var newItem by remember { mutableStateOf("") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(newItem, { newItem = it }, label = { Text("Add ingredient") },
                    modifier = Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
        item {
            Button(onClick = {},
                enabled = true,
                modifier = Modifier.fillMaxWidth()) {
                Text(if (true) "Working..." else "Generate recipes with Gemini")
            }
        }

        if (true) item { LinearProgressIndicator(Modifier.fillMaxWidth()) }
        if (true != null) item {
            Text("ERROR !!", color = MaterialTheme.colorScheme.error)
        }
        if (true) {
            item { Text("Your AI recipes", style = MaterialTheme.typography.headlineSmall) }
            item { Text("state.recipeText", style = MaterialTheme.typography.bodyLarge) }
        }
    }
}