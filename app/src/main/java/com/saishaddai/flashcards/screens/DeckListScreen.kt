package com.saishaddai.flashcards.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.decks
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.utils.getMasteryLevel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen() {
    var decksState by remember { mutableStateOf(decks) }
    val selectedDeck = decksState.find { it.isSelected }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "WELCOME BACK, DEV",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB0B0B0)
                        )
                        Text(
                            text = "What are we learning today?",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A2E)
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF1A1A2E),
            ) {
                BottomNavigationItem(text = "LEARN", icon = Icons.Default.School, selected = true)
                BottomNavigationItem(text = "DECKS", icon = Icons.Default.Style, selected = false)
                BottomNavigationItem(text = "STATS", icon = Icons.Default.BarChart, selected = false)
                BottomNavigationItem(text = "PROFILE", icon = Icons.Default.Person, selected = false)
            }
        },
        containerColor = Color(0xFF1A1A2E)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxHeight()
        ) {
            DeckGrid(
                decks = decksState,
                onDeckSelected = { deck ->
                    decksState = decksState.map {
                        it.copy(isSelected = it.id == deck.id)
                    }
                },
                modifier = Modifier.weight(1f)
            )
            StartSessionButton(
                onClick = {
                    selectedDeck?.id?.let { deckId ->
                        // You can now use the deckId
                        println(": deckId: $deckId")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
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
        Icon(Icons.Default.Navigation, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Start Session")
    }
}

@Composable
fun RowScope.BottomNavigationItem(text: String, icon: ImageVector, selected: Boolean) {
    NavigationBarItem(
        selected = selected,
        onClick = { /*TODO*/ },
        label = { Text(text, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
        icon = { Icon(icon, contentDescription = text) }
    )
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
            Icon(
                imageVector = getIconForDeck(deck.name),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = deck.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(Modifier.weight(1f))
            Column {
                if (deck.cardCount > 0) {
                    Text(
                        text = "${deck.cardCount} Cards • ${deck.mastery}%",
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
private fun getIconForDeck(name: String): ImageVector {
    return when (name) {
        "Programming" -> Icons.Default.Code
        "Android Core" -> Icons.Default.PhoneAndroid
        "Kotlin MP" -> Icons.Default.Extension
        "Security" -> Icons.Default.Security
        "Compose" -> Icons.Default.BubbleChart
        "Databases" -> Icons.Default.Storage
        "Dagger/Hilt" -> Icons.Default.Link
        "Material 3" -> Icons.Default.Palette
        else -> Icons.Default.Add
    }
}

@Preview(showBackground = true)
@Composable
fun DeckListScreenPreview() {
    Flashcards2Theme {
        DeckListScreen()
    }
}
