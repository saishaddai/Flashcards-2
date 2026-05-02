package com.saishaddai.flashcards.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.screens.commons.FullLoader
import com.saishaddai.flashcards.screens.commons.BlueButton
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import com.saishaddai.flashcards.viewmodel.FinishSessionViewModel
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import org.koin.androidx.compose.koinViewModel
import java.util.concurrent.TimeUnit

@Composable
fun FinishSessionScreen(
    deck: Deck,
    onFinishSession: () -> Unit,
    onShareSummary: (Deck) -> Unit,
    viewModel: FinishSessionViewModel = koinViewModel()
) {
    val navigateToDeckList by viewModel.navigateToDeckList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(navigateToDeckList) {
        if (navigateToDeckList) {
            onFinishSession()
            viewModel.onNavigationHandled()
        }
    }

    FinishSessionContent(
        deck = deck,
        isLoading = isLoading,
        onBackToDecksClicked = { viewModel.onBackToDecksClicked() },
        onShareSummary = onShareSummary
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishSessionContent(
    deck: Deck,
    isLoading: Boolean = false,
    onBackToDecksClicked: () -> Unit,
    onShareSummary: (Deck) -> Unit
) {
    if (isLoading) {
        FullLoader(message = stringResource(R.string.loading))
        return
    }

    val parties = remember {
        listOf(
            Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xb48def, 0xf4306d),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                position = Position.Relative(0.5, 0.3)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.finish_top_bar_title),
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackToDecksClicked) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = stringResource(R.string.finish_nav_icon_content_desc),
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onShareSummary(deck) }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.finish_action_share_content_desc),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A2E)
                )
            )
        },
        containerColor = Color(0xFF1A1A2E)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                TrophyIcon()
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.finish_all_done),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.finish_great_job, deck.name),
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoCard(
                        title = stringResource(R.string.finish_card_label_reviewed),
                        value = "20",
                        unit = stringResource(R.string.finish_card_unit_cards),
                        icon = Icons.Default.Style
                    )
                    InfoCard(
                        title = stringResource(R.string.finish_card_label_duration),
                        value = "12",
                        unit = stringResource(R.string.finish_card_unit_mins),
                        icon = Icons.Default.Timer
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                AchievementReached(
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    text = stringResource(R.string.finish_goal_reached),
                )
                Spacer(modifier = Modifier.height(16.dp))
                AchievementReached(
                    icon = Icons.AutoMirrored.Filled.Notes,
                    text = stringResource(R.string.finish_goal_reached),
                )
                Spacer(modifier = Modifier.height(16.dp))
                AchievementReached(
                    icon = Icons.AutoMirrored.Filled.StarHalf,
                    text = stringResource(R.string.finish_goal_reached),
                )
                Spacer(modifier = Modifier.height(16.dp))
                AchievementReached(
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    text = stringResource(R.string.finish_goal_reached),
                )
                Spacer(modifier = Modifier.height(32.dp))
                BackToDecksButton(onClick = onBackToDecksClicked)
                Spacer(modifier = Modifier.height(16.dp))
            }

            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = parties,
            )
        }
    }
}

@Composable
fun TrophyIcon() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(RoyalBlue)
    ) {
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(80.dp)
        )
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color(0xFFFFC700),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(30.dp)
        )
    }
}

@Composable
fun InfoCard(title: String, value: String, unit: String, icon: ImageVector) {
    Card(
        modifier = Modifier.size(150.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C4E)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = RoyalBlue)
            Text(text = title, fontSize = 12.sp, color = RoyalBlue)
            Spacer(modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = unit, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
            }
        }
    }
}

@Composable
fun AchievementReached(
    icon: ImageVector,
    contentDescription: String? = null,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0x3300BFA5))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color(0xFF00BFA5)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = RoyalBlue)
    }
}

@Composable
fun BackToDecksButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    BlueButton(
        icon = Icons.Default.GridView,
        modifier = modifier,
        text = stringResource(R.string.finish_button_back_to_decks),
        onClick = onClick
    )
}


@Preview(showBackground = true)
@Composable
fun FinishSessionScreenPreview() {
    Flashcards2Theme {
        val deck = Deck(1, "Preview Text", "preview name long version", isSelected = false)
        FinishSessionContent(
            deck = deck,
            isLoading = false,
            onBackToDecksClicked = {},
            onShareSummary = {}
        )
    }
}
