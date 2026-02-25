package com.saishaddai.flashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.saishaddai.flashcards.screens.DeckListScreen
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Flashcards2Theme {
                DeckListScreen()
            }
        }
    }
}
