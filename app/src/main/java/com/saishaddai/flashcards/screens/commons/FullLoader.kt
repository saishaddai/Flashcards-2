package com.saishaddai.flashcards.screens.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.saishaddai.flashcards.ui.theme.RoyalBlue

@Composable
fun FullLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .testTag("full_loader"),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = RoyalBlue
        )
    }
}
