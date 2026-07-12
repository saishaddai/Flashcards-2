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
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.automirrored.filled.TrendingUp
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
import com.saishaddai.flashcards.screens.commons.BlueButton
import com.saishaddai.flashcards.screens.commons.ErrorView
import com.saishaddai.flashcards.screens.commons.FullLoader
import com.saishaddai.flashcards.ui.theme.*
import com.saishaddai.flashcards.viewmodel.FinishSessionUiData
import com.saishaddai.flashcards.utils.SessionResult
import com.saishaddai.flashcards.utils.UiState
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Timer

@Composable
fun FinishSessionScreen(
    uiState: UiState<FinishSessionUiData>,
    deck: Deck,
    cardsReviewed: Int,
    startTime: Long,
    endTime: Long,
    totalTimeMillis: Long,
    onFinishSession: () -> Unit,
    onShareSummary: (Deck) -> Unit,
    onBackToDecksClicked: () -> Unit,
    onNavigationHandled: () -> Unit,
    onRetry: () -> Unit
) {
    when (uiState) {
        is UiState.Loading -> {
            FullLoader(message = stringResource(R.string.loading))
        }
        is UiState.Success -> {
            LaunchedEffect(uiState.data.navigateToDeckList) {
                if (uiState.data.navigateToDeckList) {
                    onFinishSession()
                    onNavigationHandled()
                }
            }

            FinishSessionContent(
                deck = deck,
                cardsReviewed = cardsReviewed,
                startTime = startTime,
                endTime = endTime,
                totalTimeMillis = totalTimeMillis,
                sessionResult = uiState.data.sessionResult,
                onBackToDecksClicked = onBackToDecksClicked,
                onShareSummary = onShareSummary
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
fun FinishSessionContent(
    deck: Deck,
    cardsReviewed: Int,
    startTime: Long,
    endTime: Long,
    totalTimeMillis: Long,
    sessionResult: SessionResult?,
    onBackToDecksClicked: () -> Unit,
    onShareSummary: (Deck) -> Unit
) {
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
                    containerColor = DarkBackground
                )
            )
        },
        containerColor = DarkBackground
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
                        value = cardsReviewed.toString(),
                        unit = stringResource(R.string.finish_card_unit_cards),
                        icon = Icons.Default.Style
                    )
                    val durationMins = (totalTimeMillis / (1000 * 60)).toInt().coerceAtLeast(1)
                    InfoCard(
                        title = stringResource(R.string.finish_card_label_duration),
                        value = durationMins.toString(),
                        unit = stringResource(R.string.finish_card_unit_mins),
                        icon = Icons.Default.Timer
                    )
                }

                //XP del Deck (mastery increased)
                Spacer(modifier = Modifier.height(32.dp))
                AchievementReached(
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    text = stringResource(R.string.finish_total_experience, sessionResult?.sessionProgress?.toInt() ?: 0),
                )

                //Daily Goal percentage
                Spacer(modifier = Modifier.height(16.dp))
                AchievementReached(
                    icon = Icons.Default.Star,
                    text = stringResource(
                        R.string.finish_current_level,
                        sessionResult?.masteryLevel?.let { stringResource(it.nameRes) } ?: ""
                    ),
                )
                Spacer(modifier = Modifier.height(16.dp))
                AchievementReached(
                    icon = Icons.Default.Timer,
                    text = stringResource(R.string.finish_weekly_time, 25),
                )
                Spacer(modifier = Modifier.height(16.dp))
                AchievementReached(
                    icon = Icons.AutoMirrored.Filled.FactCheck,
                    text = stringResource(R.string.finish_daily_goal_streak, 3),
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
            tint = Gold,
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
            containerColor = SurfaceDark
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
            .background(Teal.copy(alpha = 0.2f))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Teal
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
        FinishSessionScreen(
            uiState = UiState.Success(
                FinishSessionUiData(
                    sessionResult = SessionResult(5.0, 50.0, com.saishaddai.flashcards.model.MasteryLevel.SOPHOMORE),
                    navigateToDeckList = false
                )
            ),
            deck = deck,
            cardsReviewed = 20,
            startTime = 0L,
            endTime = 1000L * 60 * 12,
            totalTimeMillis = 1000L * 60 * 10,
            onFinishSession = {},
            onShareSummary = {},
            onBackToDecksClicked = {},
            onNavigationHandled = {},
            onRetry = {}
        )
    }
}
