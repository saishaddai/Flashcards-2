package com.saishaddai.flashcards.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Approval
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.screens.commons.Header
import com.saishaddai.flashcards.screens.commons.PromoWidget
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import com.saishaddai.flashcards.viewmodel.DecksViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun InstructionsScreen(
    viewModel: DecksViewModel = koinViewModel(),
    onPromoClick: (Deck) -> Unit = {},
) {
    val decksState by viewModel.decks.collectAsState()
    val promoDeck = remember(decksState) { decksState.randomOrNull() }

    // Use a content-only Composable to avoid Koin dependency in Previews
    InstructionsScreenContent(
        promoDeck = promoDeck,
        onPromoClick = onPromoClick
    )
}

/**
 * Content-only version of the Instructions Screen.
 * This refactoring allows for easier Previews and testing by separating UI from ViewModel.
 */
@Composable
fun InstructionsScreenContent(
    promoDeck: Deck?,
    onPromoClick: (Deck) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Header(
            headText = stringResource(R.string.instructions_guide_label),
            titleText = stringResource(R.string.instructions_title),
            subtitleText = stringResource(R.string.instructions_description)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Instruction Cards
        InstructionCard(
            icon = Icons.Default.CalendarToday,
            title = stringResource(R.string.instructions_daily_consistency_title),
            description = stringResource(R.string.instructions_daily_consistency_desc)
        )

        Spacer(modifier = Modifier.height(16.dp))

        InstructionCard(
            icon = Icons.Default.CloudOff,
            title = stringResource(R.string.instructions_offline_first_title),
            description = stringResource(R.string.instructions_offline_first_desc)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Special Card for Learning Sessions
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF2C2C4E)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = null,
                                tint = RoyalBlue
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(R.string.instructions_sessions_title),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.instructions_sessions_desc),
                    color = Color(0xFFB0B0B0),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Nested info items indented to the right
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    NestedInfoItem(
                        icon = Icons.Default.Visibility,
                        text = stringResource(R.string.instructions_sessions_show_response_tip)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    NestedInfoItem(
                        icon = Icons.Default.Close,
                        text = stringResource(R.string.instructions_sessions_cancel_tip)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    NestedInfoItem(
                        icon = Icons.Default.Approval,
                        text = stringResource(R.string.instructions_master_topics)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Promo Widget
        promoDeck?.let { deck ->
            PromoWidget(
                randomDeck = deck,
                onPromoClick = onPromoClick
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun InstructionCard(icon: ImageVector, title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A2E))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF2C2C4E)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = RoyalBlue
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                color = Color(0xFFB0B0B0),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun NestedInfoItem(icon: ImageVector, text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF232339)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = RoyalBlue,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 13.sp
            )
        }
    }
}

@Preview
@Composable
fun InstructionsScreenPreview() {
    Flashcards2Theme {
        InstructionsScreenContent(
            promoDeck = Deck(1, "Mock Deck", "This is a mock deck for preview"),
            onPromoClick = {},
        )
    }
}
