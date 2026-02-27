package com.saishaddai.flashcards.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.viewmodel.QuickListViewModel

@Composable
fun QuickListScreen(
    deckId: Int
) {
    val viewModel: QuickListViewModel = viewModel(
        key = "QuickListViewModel_$deckId",
        factory = viewModelFactory {
            initializer {
                QuickListViewModel(deckId)
            }
        }
    )
    val flashcards by viewModel.flashcards.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Text(
                text = "Quick Flashcard List",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(flashcards) { flashcard ->
                FlashcardItem(flashcard)
            }
        }
    }
}

@Composable
fun FlashcardItem(flashcard: Flashcard) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Question:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = flashcard.question,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Answer:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = flashcard.answer,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
