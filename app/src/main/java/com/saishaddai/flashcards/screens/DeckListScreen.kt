package com.saishaddai.flashcards.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.screens.commons.BlueButton
import com.saishaddai.flashcards.screens.commons.FullLoader
import com.saishaddai.flashcards.screens.commons.Header
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import com.saishaddai.flashcards.utils.DeckAssets
import com.saishaddai.flashcards.utils.getMasteryLevel
import com.saishaddai.flashcards.viewmodel.DecksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeckListScreen(
    onStartSessionClick: (Deck) -> Unit,
    viewModel: DecksViewModel = koinViewModel()
) {
    val decksState by viewModel.decks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    DeckListContent(
        decks = decksState,
        isLoading = isLoading,
        onDeckSelected = viewModel::onDeckSelected,
        onStartSessionClick = onStartSessionClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListContent(
    decks: List<Deck>,
    isLoading: Boolean,
    onDeckSelected: (Deck) -> Unit,
    onStartSessionClick: (Deck) -> Unit
) {
    val selectedDeck = decks.find { it.isSelected }
    var showEmptyDeckDialog by remember { mutableStateOf(false) }

    if (isLoading) {
        FullLoader()
    } else {
        if (showEmptyDeckDialog) {
            AlertDialog(
                onDismissRequest = { showEmptyDeckDialog = false },
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
                        color = Color(0xFFB0B0B0)
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showEmptyDeckDialog = false }) {
                        Text(
                            text = stringResource(R.string.empty_deck_confirm),
                            color = RoyalBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                containerColor = Color(0xFF2C2C4E),
                shape = RoundedCornerShape(28.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Header(
                headText = stringResource(R.string.decks_welcome),
                titleText = stringResource(R.string.decks_learning_today)
            )

            DeckGrid(
                decks = decks,
                onDeckSelected = onDeckSelected,
                modifier = Modifier.weight(1f)
            )

            BlueButton(
                icon = Icons.Default.Navigation,
                text = stringResource(R.string.decks_start_session_button),
                onClick = {
                    selectedDeck?.let { deck ->
                        if (deck.cardCount > 0) {
                            onStartSessionClick(deck)
                        } else {
                            showEmptyDeckDialog = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun DeckGrid(
    decks: List<Deck>,
    onDeckSelected: (Deck) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(decks) { deck ->
            DeckCard(deck = deck) {
                onDeckSelected(deck)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckCard(deck: Deck, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C4E)
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
                color = Color.White
            )
            Spacer(Modifier.weight(1f))
            Column {
                if (deck.cardCount >= 0) {
                    Text(
                        text = stringResource(R.string.decks_card_count, deck.cardCount, deck.mastery),
                        fontSize = 12.sp,
                        color = Color.Gray
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
        isLoading = false,
        onDeckSelected = {},
        onStartSessionClick = {}
    )
}
