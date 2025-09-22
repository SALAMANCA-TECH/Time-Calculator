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
import com.example.itemidentifier.data.Brand
import com.example.itemidentifier.viewmodel.MainViewModel

@Composable
fun BrandScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit
) {
    val brands by viewModel.brands.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getBrands()
    }

    LazyColumn(modifier = modifier) {
        items(brands) { brand ->
            BrandItem(brand = brand, onNavigate = onNavigate)
        }
    }
}

@Composable
fun BrandItem(brand: Brand, onNavigate: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onNavigate(brand.id) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = brand.imageUrl,
                contentDescription = brand.name,
                modifier = Modifier.size(128.dp)
            )
            Text(text = brand.name)
        }
    }
}
