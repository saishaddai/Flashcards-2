package com.saishaddai.flashcards.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.utils.getMasteryLevel
import com.saishaddai.flashcards.viewmodel.DecksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen(
    viewModel: DecksViewModel = viewModel(),
    onStartSessionClick: (Deck) -> Unit,
    onInstructionsClick: () -> Unit
) {
    val decksState by viewModel.decks.collectAsState()
    val selectedDeck = decksState.find { it.isSelected }
    var showEmptyDeckDialog by remember { mutableStateOf(false) }

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
                        color = Color(0xFF4D8EFF),
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
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = stringResource(id = R.string.decks_welcome),
                        color = Color(0xFF4D8EFF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(id = R.string.decks_learning_today),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1A1A2E)
            )
        )
        
        DeckGrid(
            decks = decksState,
            onDeckSelected = viewModel::onDeckSelected,
            modifier = Modifier.weight(1f)
        )
        
        StartSessionButton(
            onClick = {
                selectedDeck?.let { deck ->
                    if (deck.cardCount > 0) {
                        onStartSessionClick(deck)
                    } else {
                        showEmptyDeckDialog = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Composable
fun StartSessionButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D8EFF))
    ) {
        Icon(
            imageVector = Icons.Default.Navigation,
            contentDescription = stringResource(R.string.decks_start_session_button)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(R.string.decks_start_session_button))
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
        border = if (deck.isSelected) BorderStroke(2.dp, Color(0xFF4D8EFF)) else null
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
        ) {
            val icon = getIconForDeck(deck.name)
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
                    modifier = Modifier.padding(bottom = 8.dp)
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

@Composable
private fun getIconForDeck(name: String): Any {
    return when (name) {
        "OOP" -> R.drawable.oop_icon
        "Android Core" -> R.drawable.android_icon
        "Kotlin" -> R.drawable.kotlin_icon
        "Kotlin MP" -> Icons.Default.PhoneAndroid
        "Security" -> Icons.Default.Security
        "Compose" -> Icons.Default.BubbleChart
        "Databases" -> Icons.Default.Storage
        "Dagger/Hilt" -> Icons.Default.Link
        "Material 3" -> Icons.Default.Palette
        "Navigation" -> R.drawable.navigation_icon
        "Jetpack" -> R.drawable.jetpack_icon
        "Unit Test" -> R.drawable.test_icon
        "Gradle" -> R.drawable.gradle_icon
        "Android OPS" -> R.drawable.ops_icon
        "Libraries" -> R.drawable.libraries_icon
        "Design Patterns" -> R.drawable.patterns_icon
        "Coroutines" -> R.drawable.coroutines_icon
        else -> Icons.Default.Add
    }
}

@Preview
@Composable
fun DeckListScreenPreview() {
    DeckListScreen(
        onStartSessionClick = {},
        onInstructionsClick = {}
    )
}
