package com.saishaddai.flashcards.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.screens.commons.BlueButton
import com.saishaddai.flashcards.screens.commons.ErrorView
import com.saishaddai.flashcards.screens.commons.FullLoader
import com.saishaddai.flashcards.screens.commons.Header
import com.saishaddai.flashcards.ui.theme.*
import com.saishaddai.flashcards.utils.DeckAssets
import com.saishaddai.flashcards.utils.TestTags
import com.saishaddai.flashcards.utils.UiState
import com.saishaddai.flashcards.utils.getMasteryLevel
import com.saishaddai.flashcards.viewmodel.DecksViewModel
import com.saishaddai.flashcards.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeckListScreen(
    onStartSessionClick: (Deck) -> Unit,
    viewModel: DecksViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userSettings by settingsViewModel.userSettings.collectAsState()
    val quickStartEnabled = userSettings?.quickStart ?: false

    when (val state = uiState) {
        is UiState.Loading -> {
            FullLoader(stringResource(R.string.loading_decks))
        }
        is UiState.Success -> {
            DeckListContent(
                decks = state.data.decks,
                showEmptyDeckDialogState = state.data.showEmptyDeckDialog,
                quickStartEnabled = quickStartEnabled,
                onDeckSelected = viewModel::onDeckSelected,
                onStartSessionClick = onStartSessionClick,
                onDismissEmptyDeckDialog = viewModel::dismissEmptyDeckDialog,
                onTriggerEmptyDeckDialog = viewModel::onStartSession
            )
        }
        is UiState.Error -> {
            ErrorView(
                message = state.message,
                onRetry = viewModel::loadDecks
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListContent(
    decks: List<Deck>,
    showEmptyDeckDialogState: Boolean,
    quickStartEnabled: Boolean,
    onDeckSelected: (Deck) -> Unit,
    onStartSessionClick: (Deck) -> Unit,
    onDismissEmptyDeckDialog: () -> Unit,
    onTriggerEmptyDeckDialog: (Deck) -> Unit
) {
    val selectedDeck = decks.find { it.isSelected }
    val deckToQuickStart = rememberSaveable { mutableStateOf<Deck?>(null) }

    DeckListDialogs(
        emptyDeckDialogVisible = showEmptyDeckDialogState,
        onDismissEmptyDeckDialog = onDismissEmptyDeckDialog,
        quickStartDeck = deckToQuickStart.value,
        onDismissQuickStartDialog = { deckToQuickStart.value = null },
        onConfirmQuickStart = { deck ->
            deckToQuickStart.value = null
            onStartSessionClick(deck)
        }
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Header(
            headText = stringResource(R.string.decks_welcome),
            titleText = stringResource(R.string.decks_learning_today)
        )

        if (decks.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_decks_available),
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.testTag(TestTags.DECKS_EMPTY_STATE)
                )
            }
        } else {
            DeckGrid(
                decks = decks,
                onDeckSelected = onDeckSelected,
                onDeckDoubleClicked = { deck ->
                    onDeckSelected(deck)
                    if (deck.cardCount > 0) {
                        if (quickStartEnabled) {
                            onStartSessionClick(deck)
                        } else {
                            deckToQuickStart.value = deck
                        }
                    } else {
                        onTriggerEmptyDeckDialog(deck)
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }

        BlueButton(
            icon = Icons.Default.Navigation,
            text = stringResource(R.string.decks_start_session_button),
            onClick = {
                selectedDeck?.let { deck ->
                    if (deck.cardCount > 0) {
                        onStartSessionClick(deck)
                    } else {
                        onTriggerEmptyDeckDialog(deck)
                    }
                }
            }
        )
    }
}

@Composable
private fun DeckListDialogs(
    emptyDeckDialogVisible: Boolean,
    onDismissEmptyDeckDialog: () -> Unit,
    quickStartDeck: Deck?,
    onDismissQuickStartDialog: () -> Unit,
    onConfirmQuickStart: (Deck) -> Unit
) {
    if (emptyDeckDialogVisible) {
        AlertDialog(
            onDismissRequest = onDismissEmptyDeckDialog,
            title = {
                Text(
                    text = stringResource(R.string.empty_deck_title),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.empty_deck_message),
                    color = TextGray
                )
            },
            confirmButton = {
                TextButton(onClick = onDismissEmptyDeckDialog) {
                    Text(
                        text = stringResource(R.string.empty_deck_confirm),
                        color = RoyalBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = SurfaceDark,
            shape = RoundedCornerShape(28.dp)
        )
    }

    quickStartDeck?.let { deck ->
        val description = stringResource(DeckAssets.getDescriptionForDeck(deck.id))
        AlertDialog(
            onDismissRequest = onDismissQuickStartDialog,
            title = {
                Text(
                    text = stringResource(R.string.flashcard_start_dialog_title),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = description,
                    color = TextGray
                )
            },
            confirmButton = {
                TextButton(onClick = { onConfirmQuickStart(deck) }) {
                    Text(
                        text = stringResource(R.string.flashcard_start_dialog_confirm),
                        color = RoyalBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissQuickStartDialog) {
                    Text(
                        text = stringResource(R.string.flashcard_start_dialog_dismiss),
                        color = RoyalBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = SurfaceDark,
            shape = RoundedCornerShape(28.dp)
        )
    }
}

@Composable
fun DeckGrid(
    decks: List<Deck>,
    onDeckSelected: (Deck) -> Unit,
    onDeckDoubleClicked: (Deck) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(decks, key = { it.id }) { deck ->
            DeckCard(
                deck = deck,
                onClick = { onDeckSelected(deck) },
                onDoubleClick = { onDeckDoubleClicked(deck) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DeckCard(deck: Deck, onClick: () -> Unit, onDoubleClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .semantics { selected = deck.isSelected }
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceDark
        ),
        border = if (deck.isSelected) BorderStroke(2.dp, RoyalBlue) else null
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
        ) {
            val icon = DeckAssets.getIconForDeck(deck.id)
            if (icon is ImageVector) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } else if (icon is Int) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(24.dp)
                )
            }

            Text(
                text = deck.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White,
                modifier = Modifier.testTag(TestTags.DECKS_LIST_DECK_TITLE)
            )
            Spacer(Modifier.weight(1f))
            Column {
                if (deck.cardCount >= 0) {
                    Text(
                        text = if (deck.cardCount > 99) {
                            stringResource(R.string.decks_card_count_plus, deck.mastery)
                        } else {
                            stringResource(
                                R.string.decks_card_count,
                                deck.cardCount,
                                deck.mastery
                            )
                        },
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.testTag(TestTags.DECKS_LIST_DECK_COUNT)
                    )
                }
                Text(
                    text = stringResource(deck.getMasteryLevel()),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview
@Composable
fun DeckListScreenPreview() {
    DeckListContent(
        decks = listOf(
            Deck(1, "Kotlin", "Kotlin Fundamentals", mastery = 50, cardCount = 20, isSelected = true),
            Deck(2, "Android", "Android Development", mastery = 30, cardCount = 15)
        ),
        showEmptyDeckDialogState = false,
        quickStartEnabled = false,
        onDeckSelected = {},
        onStartSessionClick = {},
        onDismissEmptyDeckDialog = {},
        onTriggerEmptyDeckDialog = {}
    )
}
