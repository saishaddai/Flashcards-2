package com.saishaddai.flashcards.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.viewmodel.FlashcardViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    onCancelSessionClick: () -> Unit,
    deck: Deck
) {
    val viewModelKey = remember(deck) { "FlashcardViewModel_${deck.id}_${UUID.randomUUID()}" }

    val viewModel: FlashcardViewModel = viewModel(
        key = viewModelKey,
        factory = viewModelFactory {
            initializer {
                FlashcardViewModel(deck.id)
            }
        }
    )
    val flashcards by viewModel.flashcards.collectAsState()
    val showAnswer by viewModel.showAnswer.collectAsState()

    val pagerState = rememberPagerState(pageCount = { flashcards.size })

    // Hide answer when the user starts swiping to a new page
    LaunchedEffect(pagerState.currentPage) {
        viewModel.onPageChanged()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.flashcard_top_bar_title),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB0B0B0)
                        )
                        Text(
                            text = deck.name,
                            fontSize = 14.sp,
                            color = Color(0xFF4D8EFF)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onCancelSessionClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.flashcard_nav_icon_content_desc),
                            tint = Color.White
                        )
                    }
                },
                actions = {},
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
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressIndicator(
                current = if (flashcards.isEmpty()) 0 else pagerState.currentPage + 1,
                total = flashcards.size
            )

            Spacer(modifier = Modifier.height(32.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top,
                pageSpacing = 16.dp
            ) { page ->
                val currentCard = flashcards[page]
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Flashcard(currentCard)
                    if (showAnswer) {
                        Spacer(modifier = Modifier.height(16.dp))
                        FlashcardAnswer(currentCard)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ShowResponseButton(
                onClick = { viewModel.onShowResponseClicked() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !showAnswer
            )
            CancelSessionButton(
                onClick = onCancelSessionClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ProgressIndicator(current: Int, total: Int) {
    val progress = if (total > 0) current.toFloat() / total.toFloat() else 0f
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
                text = stringResource(
                    R.string.flashcard_progress_complete,
                    (progress * 100).toInt()
                ),
                color = Color(0xFF4D8EFF),
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF4D8EFF),
            trackColor = Color(0xFF2C2C4E)
        )
    }
}

@Composable
fun Flashcard(flashcard: Flashcard) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C4E)
        ),
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(
                text = stringResource(R.string.flashcard_card_label_question),
                color = Color(0xFF4D8EFF),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            Text(
                text = flashcard.question,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun FlashcardAnswer(flashcard: Flashcard) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3366FF)
        ),
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(
                text = stringResource(R.string.flashcard_card_label_answer),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            Text(
                text = flashcard.answer,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun ShowResponseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4D8EFF),
            disabledContainerColor = Color(0xFF4D8EFF).copy(alpha = 0.5f)
        )
    ) {
        Icon(Icons.Default.Visibility, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(R.string.flashcard_button_show_response))
    }
}

@Composable
fun CancelSessionButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(Icons.Default.Close, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(R.string.flashcard_button_cancel_session), color = Color.Gray)
    }
}


@Preview(showBackground = true)
@Composable
fun FlashcardScreenPreview() {
    Flashcards2Theme {
        FlashcardScreen(
            onCancelSessionClick = {},
            deck = Deck(1, "Preview Text", "preview name long version", isSelected = false)
        )
    }
}
