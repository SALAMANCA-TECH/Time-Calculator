package com.example.itemidentifier.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.itemidentifier.data.Answer
import com.example.itemidentifier.viewmodel.MainViewModel

@Composable
fun FinalResultsScreen(viewModel: MainViewModel) {
    val riskScore by viewModel.riskScore.collectAsState()
    val riskTier by viewModel.riskTier.collectAsState()
    val userAnswers by viewModel.userAnswers.collectAsState()
    val checklist by viewModel.checklist.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("Final Results", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Risk Score: $riskScore", fontSize = 20.sp)
                Text("Risk Tier: $riskTier", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            Text("Answer Log", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(userAnswers.toList()) { (question, answer) ->
            AnswerLogItem(question = question, answer = answer)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Red Flag Summary", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        val redFlags = userAnswers.filter { (_, answer) -> answer.flag != "None" }
        if (redFlags.isEmpty()) {
            item {
                Text("No red flags identified.")
            }
        } else {
            items(redFlags.toList()) { (question, answer) ->
                val questionDetails = checklist?.checklist?.find { it.question == question }
                RedFlagItem(question = question, answer = answer, authenticImage = questionDetails?.authentic_image ?: "", inauthenticImage = questionDetails?.inauthentic_image ?: "")
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            DisclaimerAndAdvice()
        }
    }
}

@Composable
fun AnswerLogItem(question: String, answer: Answer) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(question, fontWeight = FontWeight.Bold)
            Text("Your answer: ${answer.text} (Score: ${answer.score}, Flag: ${answer.flag})")
        }
    }
}

@Composable
fun RedFlagItem(question: String, answer: Answer, authenticImage: String, inauthenticImage: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(question, fontWeight = FontWeight.Bold)
            Text("Your answer: ${answer.text}", color = Color.Red)
            Text("Flag: ${answer.flag}", color = Color.Red, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Authentic")
                    AsyncImage(model = authenticImage, contentDescription = "Authentic Image", modifier = Modifier.size(128.dp))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Inauthentic")
                    AsyncImage(model = inauthenticImage, contentDescription = "Inauthentic Image", modifier = Modifier.size(128.dp))
                }
            }
        }
    }
}

@Composable
fun DisclaimerAndAdvice() {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Disclaimer", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("This is a preliminary self-assessment tool. The results are not a guarantee of authenticity. For a definitive evaluation, please consult a professional authenticator.")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Actionable Advice", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("We strongly recommend seeking a final evaluation from a professional authenticator, especially if any red flags were identified.")
        }
    }
}
