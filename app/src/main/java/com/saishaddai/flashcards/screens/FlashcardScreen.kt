package com.saishaddai.flashcards.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.screens.commons.BlueButton
import com.saishaddai.flashcards.screens.commons.ErrorView
import com.saishaddai.flashcards.screens.commons.FullLoader
import com.saishaddai.flashcards.screens.commons.TransparentButton
import com.saishaddai.flashcards.ui.theme.DarkBackground
import com.saishaddai.flashcards.ui.theme.ElectricBlue
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import com.saishaddai.flashcards.ui.theme.SurfaceDark
import com.saishaddai.flashcards.ui.theme.TextGray
import com.saishaddai.flashcards.utils.UiState
import com.saishaddai.flashcards.viewmodel.FlashcardsUiData

@Composable
fun FlashcardScreen(
    uiState: UiState<FlashcardsUiData>,
    deck: Deck,
    onShowResponseClicked: () -> Unit,
    onPageChanged: (Int) -> Unit,
    onFinishSession: (Int) -> Unit,
    onCancelSessionClick: () -> Unit,
    onFinishedSessionClick: () -> Unit,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onRetry: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> onStartTimer()
                Lifecycle.Event.ON_PAUSE -> onPauseTimer()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    when (uiState) {
        is UiState.Loading -> {
            FullLoader(message = stringResource(R.string.loading))
        }
        is UiState.Success -> {
            FlashcardContent(
                deck = deck,
                flashcards = uiState.data.flashcards,
                showAnswer = uiState.data.showAnswer,
                isFinished = uiState.data.isFinished,
                onShowResponseClicked = onShowResponseClicked,
                onPageChanged = onPageChanged,
                onFinishSession = onFinishSession,
                onCancelSessionClick = onCancelSessionClick,
                onFinishedSessionClick = onFinishedSessionClick
            )
        }
        is UiState.Error -> {
            ErrorView(
                message = uiState.message,
                onRetry = onRetry
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardContent(
    deck: Deck,
    flashcards: List<Flashcard>,
    showAnswer: Boolean,
    isFinished: Boolean,
    onShowResponseClicked: () -> Unit,
    onPageChanged: (Int) -> Unit,
    onFinishSession: (Int) -> Unit,
    onCancelSessionClick: () -> Unit,
    onFinishedSessionClick: () -> Unit
) {
    if (isFinished) {
        onFinishedSessionClick()
        return
    }

    val pagerState = rememberPagerState(pageCount = { flashcards.size })
    val isLastPage = if (flashcards.isEmpty()) true else pagerState.currentPage == flashcards.size - 1
    val showCancelConfirmation = rememberSaveable { mutableStateOf(false) }

    // Hide answer when the user starts swiping to a new page
    LaunchedEffect(pagerState.currentPage) {
        if (flashcards.isNotEmpty()) {
            onPageChanged(pagerState.currentPage)
        }
    }

    if (showCancelConfirmation.value) {
        AlertDialog(
            onDismissRequest = { showCancelConfirmation.value = false },
            title = {
                Text(
                    text = stringResource(R.string.flashcard_cancel_dialog_title),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.flashcard_cancel_dialog_message),
                    color = TextGray
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showCancelConfirmation.value = false
                    onCancelSessionClick()
                }) {
                    Text(
                        text = stringResource(R.string.flashcard_cancel_dialog_confirm),
                        color = RoyalBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelConfirmation.value = false }) {
                    Text(
                        text = stringResource(R.string.flashcard_cancel_dialog_dismiss),
                        color = RoyalBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = SurfaceDark,
            shape = RoundedCornerShape(28.dp)
        )
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
                            color = TextGray
                        )
                        Text(
                            text = deck.name,
                            fontSize = 14.sp,
                            color = RoyalBlue
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isLastPage) onFinishedSessionClick() else showCancelConfirmation.value = true
                    }) {
                        Icon(
                            imageVector = if (isLastPage) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = stringResource(
                                id = if (isLastPage) R.string.flashcard_nav_icon_content_desc_finish else R.string.flashcard_nav_icon_content_desc_close
                            ),
                            tint = Color.White
                        )
                    }
                },
                actions = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        },
        containerColor = DarkBackground
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
                    Flashcard(currentCard, onClick = onShowResponseClicked)
                    if (showAnswer) {
                        Spacer(modifier = Modifier.height(16.dp))
                        FlashcardAnswer(currentCard)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ShowResponseButton(
                onClick = onShowResponseClicked,
                modifier = Modifier.fillMaxWidth(),
                enabled = !showAnswer
            )

            if (isLastPage) {
                BlueButton(
                    icon = Icons.Default.Check,
                    text = stringResource(R.string.flashcard_button_finish_session),
                    onClick = { onFinishSession(pagerState.currentPage) }
                )
            } else {
                CancelSessionButton(
                    onClick = { showCancelConfirmation.value = true },
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
                color = RoyalBlue,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = RoyalBlue,
            trackColor = SurfaceDark
        )
    }
}

@Composable
fun Flashcard(flashcard: Flashcard, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(enabled = true, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceDark
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.flashcard_card_label_question),
                color = RoyalBlue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = flashcard.question,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }
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
            containerColor = ElectricBlue
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.flashcard_card_label_answer),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = flashcard.answer,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            }
        }
    }
}

@Composable
fun ShowResponseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    BlueButton(
        Icons.Default.Visibility,
        modifier = modifier,
        text = stringResource(R.string.flashcard_button_show_response),
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
fun CancelSessionButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    TransparentButton(
        icon = Icons.Default.Close,
        text = stringResource(R.string.flashcard_button_cancel_session),
        onClick = onClick,
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun FlashcardScreenPreview() {
    Flashcards2Theme {
        FlashcardScreen(
            uiState = UiState.Success(
                FlashcardsUiData(
                    flashcards = listOf(
                        Flashcard(deckId = 1, id = 1, question = "What is Kotlin?", answer = "A modern programming language."),
                        Flashcard(deckId = 1, id = 2, question = "What is Jetpack Compose?", answer = "A modern toolkit for building native UI.")
                    ),
                    showAnswer = true,
                    isFinished = false
                )
            ),
            deck = Deck(id = 1, name = "Preview Text", longName = "preview name long version", isSelected = false),
            onShowResponseClicked = {},
            onPageChanged = {},
            onFinishSession = {},
            onCancelSessionClick = {},
            onFinishedSessionClick = {},
            onStartTimer = {},
            onPauseTimer = {},
            onRetry = {}
        )
    }
}
