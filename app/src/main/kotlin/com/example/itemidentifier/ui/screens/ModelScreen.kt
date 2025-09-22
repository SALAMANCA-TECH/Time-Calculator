package com.example.itemidentifier.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.itemidentifier.data.Model
import com.example.itemidentifier.viewmodel.MainViewModel

@Composable
fun ModelScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    categoryId: String
) {
    val models by viewModel.models.collectAsState()

    LaunchedEffect(categoryId) {
        viewModel.getModels(categoryId)
    }

    LazyColumn(modifier = modifier) {
        items(models) { model ->
            ModelItem(
                model = model,
                onModelSelected = { checklistId ->
                    Log.d("ModelScreen", "Selected checklist: $checklistId")
                    // TODO: Load the checklist from Firestore
                }
            )
        }
    }
}

@Composable
fun ModelItem(model: Model, onModelSelected: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onModelSelected(model.checklistId) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = model.imageUrl,
                contentDescription = model.name,
                modifier = Modifier.size(128.dp)
            )
            Text(text = model.name)
        }
    }
}
