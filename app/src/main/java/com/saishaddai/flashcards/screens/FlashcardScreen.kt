package com.saishaddai.flashcards.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "ANDROID DEVELOPMENT",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB0B0B0)
                        )
                        Text(
                            text = "Jetpack Compose Basics",
                            fontSize = 14.sp,
                            color = Color(0xFF4D8EFF)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Implement close */ }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement settings */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A2E)
                )
            )
        },
        containerColor = Color(0xFF1A1A2E)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressIndicator(5, 20)
            Spacer(modifier = Modifier.height(32.dp))
            Flashcard()
            Spacer(modifier = Modifier.weight(1f))
            ShowResponseButton(modifier = Modifier.fillMaxWidth())
            CancelSessionButton(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun ProgressIndicator(current: Int, total: Int) {
    val progress = current.toFloat() / total.toFloat()
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$current/$total",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${(progress * 100).toInt()}% Complete",
                color = Color(0xFF4D8EFF),
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF4D8EFF),
            trackColor = Color(0xFF2C2C4E)
        )
    }
}

@Composable
fun Flashcard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C4E)
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "QUESTION",
                color = Color(0xFF4D8EFF),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "What is a Composable function?",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            // TODO: Replace with actual icon
            Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = null,
                tint = Color(0xFF4D8EFF),
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "Tap below to reveal the answer", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun ShowResponseButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { /*TODO*/ },
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D8EFF))
    ) {
        Icon(Icons.Default.Visibility, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Show Response")
    }
}

@Composable
fun CancelSessionButton(modifier: Modifier = Modifier) {
    TextButton(
        onClick = { /*TODO*/ },
        modifier = modifier
    ) {
        Icon(Icons.Default.Close, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "CANCEL SESSION", color = Color.Gray)
    }
}


@Preview(showBackground = true)
@Composable
fun FlashcardScreenPreview() {
    Flashcards2Theme {
        FlashcardScreen()
    }
}
