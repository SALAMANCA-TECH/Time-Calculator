package com.example.itemidentifier.ui.screens

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
import com.example.itemidentifier.data.Category
import com.example.itemidentifier.viewmodel.MainViewModel

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    brandId: String,
    onNavigate: (String) -> Unit
) {
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(brandId) {
        viewModel.getCategories(brandId)
    }

    LazyColumn(modifier = modifier) {
        items(categories) { category ->
            CategoryItem(category = category, onNavigate = onNavigate)
        }
    }
}

@Composable
fun CategoryItem(category: Category, onNavigate: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onNavigate(category.id) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.name,
                modifier = Modifier.size(128.dp)
            )
            Text(text = category.name)
        }
    }
}
