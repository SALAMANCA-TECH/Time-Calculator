package com.example.itemidentifier.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.itemidentifier.data.Brand
import com.example.itemidentifier.data.Category
import com.example.itemidentifier.data.Model
import com.example.itemidentifier.repository.DataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = DataRepository()

    private val _brands = MutableStateFlow<List<Brand>>(emptyList())
    val brands: StateFlow<List<Brand>> = _brands

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _models = MutableStateFlow<List<Model>>(emptyList())
    val models: StateFlow<List<Model>> = _models

    fun getBrands() {
        viewModelScope.launch {
            _brands.value = repository.getBrands()
        }
    }

    fun getCategories(brandId: String) {
        viewModelScope.launch {
            _categories.value = repository.getCategories(brandId)
        }
    }

    fun getModels(categoryId: String) {
        viewModelScope.launch {
            _models.value = repository.getModels(categoryId)
        }
    }
}
