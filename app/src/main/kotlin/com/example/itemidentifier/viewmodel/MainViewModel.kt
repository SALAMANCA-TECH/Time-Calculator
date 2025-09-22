package com.example.itemidentifier.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.itemidentifier.data.*
import com.example.itemidentifier.repository.DataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class RiskTier {
    Low, Medium, High, Undetermined
}

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

    private val _riskScore = MutableStateFlow(0)
    val riskScore: StateFlow<Int> = _riskScore

    private val _riskTier = MutableStateFlow(RiskTier.Undetermined)
    val riskTier: StateFlow<RiskTier> = _riskTier

    private val _assessmentHalted = MutableStateFlow(false)
    val assessmentHalted: StateFlow<Boolean> = _assessmentHalted

    private val userAnswers = mutableMapOf<String, Answer>()

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
            _riskScore.value = 0
            _riskTier.value = RiskTier.Undetermined
            _assessmentHalted.value = false
        }
    }

    fun answerQuestion(question: String, answer: Answer) {
        if (_assessmentHalted.value) return

        userAnswers[question] = answer
        _riskScore.value += answer.score

        Log.d("MainViewModel", "User answered question: '$question' with '${answer.text}' -> score: ${answer.score}, flag: ${answer.flag}")

        if (answer.flag == "Definitive") {
            _assessmentHalted.value = true
            updateRiskTier()
            return
        }

        _currentQuestionIndex.value++
        if (_currentQuestionIndex.value >= (_checklist.value?.checklist?.size ?: 0)) {
            updateRiskTier()
        }
    }

    private fun updateRiskTier() {
        _riskTier.value = when (_riskScore.value) {
            in 0..30 -> RiskTier.Low
            in 31..70 -> RiskTier.Medium
            else -> RiskTier.High
        }
    }
}
