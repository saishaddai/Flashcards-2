package com.saishaddai.flashcards.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.screens.commons.FullLoader
import com.saishaddai.flashcards.screens.commons.Header
import com.saishaddai.flashcards.screens.commons.PromoWidget
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import com.saishaddai.flashcards.utils.TestTags
import com.saishaddai.flashcards.viewmodel.DecksViewModel
import com.saishaddai.flashcards.viewmodel.SettingsViewModel
import com.saishaddai.flashcards.viewmodel.StatsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StatsScreen(
    decksViewModel: DecksViewModel = koinViewModel(),
    statsViewModel: StatsViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel(),
    onPromoClick: (Deck) -> Unit = {},
) {
    val promoDeck = decksViewModel.getRandomDeck()
    val weeklyActivity by statsViewModel.weeklyActivity.collectAsState()
    val skillMastery by statsViewModel.skillMastery.collectAsState()
    val cardsReviewed by statsViewModel.cardsReviewed.collectAsState()
    val currentStreak by statsViewModel.currentStreak.collectAsState()
    val studyTime by statsViewModel.studyTime.collectAsState()
    val accuracyRate by statsViewModel.accuracyRate.collectAsState()
    val isLoading by statsViewModel.isLoading.collectAsState()
    val userSettings by settingsViewModel.userSettings.collectAsState()
    val showSuggestions = userSettings?.showSuggestions ?: true

    StatsContent(
        promoDeck = promoDeck,
        weeklyActivity = weeklyActivity,
        skillMastery = skillMastery,
        cardsReviewed = cardsReviewed,
        currentStreak = currentStreak,
        studyTime = studyTime,
        accuracyRate = accuracyRate,
        isLoading = isLoading,
        showSuggestions = showSuggestions,
        onBackClicked = statsViewModel::onBackClicked,
        onShareClicked = statsViewModel::onShareClicked,
        onMoreOptionsClicked = statsViewModel::onMoreOptionsClicked,
        onViewAllSkillsClicked = statsViewModel::onViewAllSkillsClicked,
        onPromoClick = onPromoClick
    )
}

@Composable
fun StatsContent(
    promoDeck: Deck?,
    weeklyActivity: List<Int>,
    skillMastery: List<MasteryData>,
    cardsReviewed: String,
    currentStreak: String,
    studyTime: String,
    accuracyRate: String,
    isLoading: Boolean,
    showSuggestions: Boolean,
    onBackClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onMoreOptionsClicked: () -> Unit,
    onViewAllSkillsClicked: () -> Unit,
    onPromoClick: (Deck) -> Unit,
) {
    if (isLoading) {
        FullLoader(message = stringResource(R.string.loading_stats))
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A2E))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // Top Actions Row from Mockup
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Row {
                    IconButton(onClick = onShareClicked) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onMoreOptionsClicked) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.White
                        )
                    }
                }
            }

            Header(
                headText = stringResource(R.string.stats_head_title),
                titleText = stringResource(R.string.stats_title),
                subtitleText = stringResource(R.string.stats_subtitle)
            )

            Spacer(modifier = Modifier.height(24.dp))
            WeeklyActivityCard(weeklyActivity)
            Spacer(modifier = Modifier.height(32.dp))
            SkillMasterySection(skillMastery, onViewAllSkillsClicked)
            Spacer(modifier = Modifier.height(32.dp))
            AtAGlanceSection(cardsReviewed, currentStreak, studyTime, accuracyRate)
            Spacer(modifier = Modifier.height(32.dp))

            if (showSuggestions && promoDeck != null) {
                PromoWidget(
                    randomDeck = promoDeck,
                    onPromoClick = onPromoClick
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun WeeklyActivityCard(activityData: List<Int>) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(activityData) {
        modelProducer.runTransaction {
            columnSeries { series(activityData) }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161D31))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Weekly Activity",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Oct 23 - Oct 29",
                        fontSize = 14.sp,
                        color = Color(0xFFB0B0B0)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = activityData.sum().toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = RoyalBlue
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+12%",
                            fontSize = 12.sp,
                            color = Color(0xFF10B981),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProvideVicoTheme(rememberM3VicoTheme()) {
                CartesianChartHost(
                    chart = rememberCartesianChart(
                        rememberColumnCartesianLayer(
                            columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                                rememberLineComponent(
                                    fill = fill(RoyalBlue),
                                    thickness = 12.dp,
                                    shape = CorneredShape.Pill
                                )
                            )
                        ),
                        bottomAxis = HorizontalAxis.rememberBottom(
                            valueFormatter = { _, value, _ ->
                                listOf("M", "T", "W", "T", "F", "S", "S")[value.toInt() % 7]
                            },
                            guideline = null,
                            label = rememberAxisLabelComponent(
                                color = Color(0xFFB0B0B0),
                                textSize = 12.sp
                            )
                        ),
                    ),
                    modelProducer = modelProducer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .testTag(TestTags.STATS_WEEKLY_ACTIVITY)
                )
            }
        }
    }
}

@Composable
fun SkillMasterySection(masteryList: List<MasteryData>, onViewAllClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Skill Mastery",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFFB0B0B0),
                    modifier = Modifier.size(18.dp)
                )
            }
            TextButton(onClick = onViewAllClick) {
                Text(text = "View all", color = RoyalBlue, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            contentPadding = PaddingValues(end = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(masteryList) { data ->
                SkillCard(data)
            }
        }
    }
}

data class MasteryData(val title: String, val percentage: Int, val level: String, val color: Color)

@Composable
fun SkillCard(data: MasteryData) {
    Card(
        modifier = Modifier.size(width = 160.dp, height = 180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161D31))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { data.percentage / 100f },
                    modifier = Modifier.size(80.dp),
                    color = data.color,
                    strokeWidth = 8.dp,
                    trackColor = Color(0xFF2C2C4E),
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = "${data.percentage}%",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = data.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = data.level,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB0B0B0)
            )
        }
    }
}

@Composable
fun AtAGlanceSection(
    cardsReviewed: String,
    currentStreak: String,
    studyTime: String,
    accuracyRate: String
) {
    Column {
        Text(
            text = "At a Glance",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Style,
                    value = cardsReviewed,
                    label = "Cards Reviewed",
                    containerColor = Color(0xFF1E293B),
                    iconColor = RoyalBlue
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.LocalFireDepartment,
                    value = currentStreak,
                    label = "Current Streak",
                    containerColor = Color(0xFF3E2723),
                    iconColor = Color(0xFFF59E0B)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.AccessTime,
                    value = studyTime,
                    label = "Study Time",
                    containerColor = Color(0xFF2E1065),
                    iconColor = Color(0xFF8B5CF6)
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.CheckCircle,
                    value = accuracyRate,
                    label = "Accuracy Rate",
                    containerColor = Color(0xFF064E3B),
                    iconColor = Color(0xFF10B981)
                )
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    containerColor: Color,
    iconColor: Color
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = Color(0xFFB0B0B0)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatsScreenPreview() {
    Flashcards2Theme {
        StatsContent(
            promoDeck = Deck(1, "Kotlin", "Kotlin Fundamentals", isSelected = false),
            weeklyActivity = listOf(10, 20, 15, 30, 25, 40, 35),
            skillMastery = listOf(
                MasteryData("Language", 85, "Advanced", RoyalBlue),
                MasteryData("UI/UX", 60, "Intermediate", Color(0xFFF59E0B))
            ),
            cardsReviewed = "1,234",
            currentStreak = "7",
            studyTime = "12h 30m",
            accuracyRate = "92%",
            isLoading = false,
            showSuggestions = true,
            onBackClicked = {},
            onShareClicked = {},
            onMoreOptionsClicked = {},
            onViewAllSkillsClicked = {},
            onPromoClick = {}
        )
    }
}
