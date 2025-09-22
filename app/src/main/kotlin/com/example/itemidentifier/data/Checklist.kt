package com.example.itemidentifier.data

data class Checklist(
    val brand: String,
    val category: String,
    val model: String,
    val checklist: List<ChecklistItem>
)

data class ChecklistItem(
    val question: String,
    val answers: List<Answer>,
    val inauthentic_image: String,
    val authentic_image: String
)

data class Answer(
    val text: String,
    val flag: String,
    val score: Int
)
