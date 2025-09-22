package com.example.itemidentifier.repository

import android.content.Context
import com.example.itemidentifier.data.Brand
import com.example.itemidentifier.data.Category
import com.example.itemidentifier.data.Checklist
import com.example.itemidentifier.data.Model
import com.example.spla.FirebaseManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class DataRepository(context: Context) {

    private val db = Firebase.firestore
    private val firebaseManager = FirebaseManager(context)

    suspend fun getBrands(): List<Brand> {
        return try {
            db.collection("brands").get().await().toObjects(Brand::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getCategories(brandId: String): List<Category> {
        return try {
            db.collection("brands").document(brandId).collection("categories").get().await().toObjects(Category::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getModels(categoryId: String): List<Model> {
        return try {
            db.collectionGroup("categories").whereEqualTo("id", categoryId).get().await().documents.firstOrNull()?.reference?.collection("models")?.get()?.await()?.toObjects(Model::class.java) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getChecklist(brand: String, category: String, model: String): Checklist? {
        return firebaseManager.getChecklist(brand, category, model)
    }
}
