package com.example.itemidentifier.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.itemidentifier.viewmodel.MainViewModel
import com.example.itemidentifier.viewmodel.RiskTier

@Composable
fun DiagnosticFunnelScreen(
    viewModel: MainViewModel,
    modelId: String
) {
    val checklist by viewModel.checklist.collectAsState()
    val currentQuestionIndex by viewModel.currentQuestionIndex.collectAsState()
    val riskScore by viewModel.riskScore.collectAsState()
    val riskTier by viewModel.riskTier.collectAsState()
    val assessmentHalted by viewModel.assessmentHalted.collectAsState()

    LaunchedEffect(modelId) {
        // This is a hack to get the brand, category, and model from the modelId
        // In a real app, you would have a better way of doing this.
        val parts = modelId.split("_")
        val brand = parts[0]
        val category = parts[1]
        val model = parts.subList(2, parts.size).joinToString(" ")
        viewModel.getChecklist(brand, category, model)
    }

    if (checklist == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val questions = checklist!!.checklist
        if (currentQuestionIndex < questions.size && !assessmentHalted) {
            val question = questions[currentQuestionIndex]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = question.question)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Authentic")
                        AsyncImage(
                            model = question.authentic_image,
                            contentDescription = "Authentic Image",
                            modifier = Modifier.size(128.dp)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Inauthentic")
                        AsyncImage(
                            model = question.inauthentic_image,
                            contentDescription = "Inauthentic Image",
                            modifier = Modifier.size(128.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    question.answers.forEach { answer ->
                        Button(onClick = { viewModel.answerQuestion(question.question, answer) }) {
                            Text(text = answer.text)
                        }
                    }
                }
            }
        } else {
            FunnelResultsScreen(riskScore = riskScore, riskTier = riskTier, assessmentHalted = assessmentHalted)
        }
    }
}

@Composable
fun FunnelResultsScreen(riskScore: Int, riskTier: RiskTier, assessmentHalted: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (assessmentHalted) {
            Text(text = "Assessment Halted!", fontSize = 24.sp, color = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
        }
        Text(text = "Funnel Complete!", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Final Score: $riskScore", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Risk Tier: $riskTier", fontSize = 20.sp)
    }
}
