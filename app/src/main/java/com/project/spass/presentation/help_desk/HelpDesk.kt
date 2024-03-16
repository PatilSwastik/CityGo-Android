package com.project.spass.presentation.help_desk


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HelpDesk() {
    var promptText by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("") }
    var isFetching by remember { mutableStateOf(false) }
    val LocalFocusManager = LocalFocusManager.current


    val questionResponseList = remember { mutableStateListOf<Pair<String, String>>() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(850.dp)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(bottom = 70.dp)
                    .background(Color.White),
                reverseLayout = true
            ) {
                items(questionResponseList.reversed()) { (question, response) ->
                    DisplayResponse(question = question, response = response)
                }
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            TextField(
                value = promptText,
                onValueChange = { promptText = it },
                label = { Text("How may I help you?") },
                modifier = Modifier
                    .weight(1f)
                    .padding(15.dp)
                    .padding(bottom = 10.dp)
                    .padding(end = 10.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            if (isFetching) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 30.dp, end = 30.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp)
                    )
                }
            } else {
                Button(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .padding(bottom = 30.dp, end = 20.dp)
                        .height(IntrinsicSize.Min),
                    onClick = {
                        LocalFocusManager.clearFocus()
                        if (promptText.isEmpty()) return@Button
                        else
                            isFetching = true
                        CoroutineScope(Dispatchers.IO).fetchResponse(promptText) { response ->
                            responseText = response
                            isFetching = false
                            // Add the question and response to the list
                            questionResponseList.add(promptText to responseText)
                            // Clear promptText after adding the question
                            promptText = ""
                        }
                    },
                ) {
                    Text("Ask")
                }
            }
        }
    }
}

private fun CoroutineScope.fetchResponse(prompt: String, onResponseReceived: (String) -> Unit) {
    launch(Dispatchers.IO) {
        try {
            val response = getGeminiResponse(prompt)
            onResponseReceived(response)
        } catch (e: Exception) {
        }
    }
}

suspend fun getGeminiResponse(prompt: String): String {
    val generativeModel = GenerativeModel(
        modelName = "gemini-pro", apiKey = "AIzaSyDVaxIb8HyTFbX1HxEBVF7N-ZD87uVzh44"
    )

    val response = generativeModel.generateContent(prompt)

    return response.text ?: ""
}

@Composable
fun DisplayResponse(question: String, response: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(text = "Question: $question", color = Color.Black)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Response: $response", color = Color.Black)
        }
    }
}
