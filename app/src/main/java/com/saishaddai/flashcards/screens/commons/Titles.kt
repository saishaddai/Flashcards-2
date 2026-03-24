package com.saishaddai.flashcards.screens.commons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saishaddai.flashcards.ui.theme.RoyalBlue


@Composable
fun Header(
    headText: String,
    titleText: String,
    subtitleText: String? = null
) {
    Column {
        Head(headText)
        Title(titleText)
        subtitleText?.let { text ->
            Spacer(modifier = Modifier.height(16.dp))
            Subtitle(text)
        }
    }
}


@Composable
fun Head(text: String) {
    Text(
        text = text,
        color = RoyalBlue,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun Title(text: String) {
    Text(
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
}

@Composable
fun Subtitle(text: String) {
    Text(
        text = text,
        color = Color(0xFFB0B0B0),
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
}

@Preview
@Composable
fun PreviewHeader() {
    Header(
        headText = "HEADER TEXT",
        titleText = "Title Text",
        subtitleText = "Subtitle long explanations of this section"
    )
}

@Preview
@Composable
fun PreviewHeaderNoSubtitle() {
    Header(
        headText = "HEADER TEXT",
        titleText = "Title Text"
    )
}

