package com.example.itemidentifier.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.itemidentifier.data.Brand
import com.example.itemidentifier.data.Category
import com.example.itemidentifier.data.Checklist
import com.example.itemidentifier.data.Model
import com.example.itemidentifier.repository.DataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DataRepository(application)

    private val _brands = MutableStateFlow<List<Brand>>(emptyList())
    val brands: StateFlow<List<Brand>> = _brands

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _models = MutableStateFlow<List<Model>>(emptyList())
    val models: StateFlow<List<Model>> = _models

    private val _checklist = MutableStateFlow<Checklist?>(null)
    val checklist: StateFlow<Checklist?> = _checklist

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex

    private val userAnswers = mutableMapOf<String, String>()

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

    fun getChecklist(brand: String, category: String, model: String) {
        viewModelScope.launch {
            _checklist.value = repository.getChecklist(brand, category, model)
            _currentQuestionIndex.value = 0
            userAnswers.clear()
        }
    }

    fun answerQuestion(question: String, answer: String) {
        userAnswers[question] = answer
        Log.d("MainViewModel", "User answered question: '$question' with '$answer'")
        _currentQuestionIndex.value++
    }
}
