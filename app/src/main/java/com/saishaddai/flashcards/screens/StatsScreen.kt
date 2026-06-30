package com.saishaddai.flashcards.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.ProvideVicoTheme
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.m3.common.rememberM3VicoTheme
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.screens.commons.ErrorView
import com.saishaddai.flashcards.screens.commons.FullLoader
import com.saishaddai.flashcards.screens.commons.Header
import com.saishaddai.flashcards.screens.commons.PromoWidget
import com.saishaddai.flashcards.ui.theme.DarkBackground
import com.saishaddai.flashcards.ui.theme.DeepBlue
import com.saishaddai.flashcards.ui.theme.DeepBrown
import com.saishaddai.flashcards.ui.theme.DeepGreen
import com.saishaddai.flashcards.ui.theme.ErrorRed
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.ui.theme.Indigo
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import com.saishaddai.flashcards.ui.theme.SlateBlue
import com.saishaddai.flashcards.ui.theme.SoftPurple
import com.saishaddai.flashcards.ui.theme.SuccessGreen
import com.saishaddai.flashcards.ui.theme.SurfaceDark
import com.saishaddai.flashcards.ui.theme.TextGray
import com.saishaddai.flashcards.ui.theme.WarningOrange
import com.saishaddai.flashcards.utils.TestTags
import com.saishaddai.flashcards.utils.UiState
import com.saishaddai.flashcards.viewmodel.StatsUiState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

val ColorKey = SemanticsPropertyKey<Color>("Color")
var SemanticsPropertyReceiver.colorProperty by ColorKey

@Composable
fun StatsScreen(
    uiState: UiState<StatsUiState>,
    promoDeck: Deck?,
    showSuggestions: Boolean,
    onViewAllSkillsClicked: () -> Unit,
    onInfoClick: (String, String) -> Unit,
    onDismissInfoDialog: () -> Unit,
    onRetry: () -> Unit,
    onPromoClick: (Deck) -> Unit,
) {
    when (uiState) {
        is UiState.Loading -> {
            FullLoader(message = stringResource(R.string.loading_stats))
        }

        is UiState.Success -> {
            StatsContent(
                promoDeck = promoDeck,
                weeklyActivity = uiState.data.weeklyActivity,
                skillMastery = uiState.data.skillMastery,
                flashcardsViewed = uiState.data.flashcardsViewed,
                currentStreak = uiState.data.currentStreak,
                studyTime = uiState.data.studyTime,
                masteredDecks = uiState.data.masteredDecks,
                weeklyComparison = uiState.data.weeklyComparison,
                showSuggestions = showSuggestions,
                infoDialogContent = uiState.data.infoDialogContent,
                isSkillsExpanded = uiState.data.isSkillsExpanded,
                onViewAllSkillsClicked = onViewAllSkillsClicked,
                onInfoClick = onInfoClick,
                onDismissInfoDialog = onDismissInfoDialog,
                onPromoClick = onPromoClick,
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

@Composable
fun StatsContent(
    promoDeck: Deck?,
    weeklyActivity: List<Int>,
    skillMastery: List<MasteryData>,
    flashcardsViewed: String,
    currentStreak: String,
    studyTime: String,
    masteredDecks: String,
    weeklyComparison: Int,
    showSuggestions: Boolean,
    infoDialogContent: Pair<String, String>?,
    isSkillsExpanded: Boolean,
    onViewAllSkillsClicked: () -> Unit,
    onInfoClick: (String, String) -> Unit,
    onDismissInfoDialog: () -> Unit,
    onPromoClick: (Deck) -> Unit,
) {
    infoDialogContent?.let { (title, desc) ->
        StatsInfoDialog(
            title = title,
            description = desc,
            onDismiss = onDismissInfoDialog
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Header(
            headText = stringResource(R.string.stats_head_title),
            titleText = stringResource(R.string.stats_title)
        )

        val weeklyActivityTitle = stringResource(R.string.stats_weekly_activity_info_title)
        val weeklyActivityDesc = stringResource(R.string.stats_weekly_activity_info_desc)
        val skillMasteryTitle = stringResource(R.string.stats_skill_mastery_info_title)
        val skillMasteryDesc = stringResource(R.string.stats_skill_mastery_info_desc)

        Spacer(modifier = Modifier.height(24.dp))
        WeeklyActivityCard(
            activityData = weeklyActivity,
            weeklyComparison = weeklyComparison,
            onInfoClick = { onInfoClick(weeklyActivityTitle, weeklyActivityDesc) },
        )
        Spacer(modifier = Modifier.height(24.dp))
        SkillMasterySection(
            masteryList = skillMastery,
            isExpanded = isSkillsExpanded,
            onViewAllClick = onViewAllSkillsClicked,
            onInfoClick = { onInfoClick(skillMasteryTitle, skillMasteryDesc) }
        )
        Spacer(modifier = Modifier.height(24.dp))
        AtAGlanceSection(flashcardsViewed, currentStreak, studyTime, masteredDecks)
        Spacer(modifier = Modifier.height(32.dp))

        if (showSuggestions && (promoDeck != null)) {
            PromoWidget(
                randomDeck = promoDeck,
                onPromoClick = onPromoClick
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun WeeklyActivityCard(activityData: List<Int>, weeklyComparison: Int, onInfoClick: () -> Unit) {
    val dateRange = remember { getWeeklyDateRange() }

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(activityData) {
        modelProducer.runTransaction {
            columnModel { series(activityData) }
        }
    }

    Column {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.stats_title_weekly_activity),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = TextGray,
                modifier = Modifier
                    .size(18.dp)
                    .testTag(TestTags.STATS_WEEKLY_ACTIVITY_DESCRIPTION)
                    .clickable { onInfoClick() }
            )
        }
        Text(
            text = dateRange,
            fontSize = 14.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = DeepBlue)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = activityData.sum().toString(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlue,
                            modifier = Modifier.testTag(TestTags.STATS_WEEKLY_ACTIVITY_TOTAL)
                        )
                        Text(
                            text = stringResource(R.string.stats_cards_reviewed),
                            fontSize = 12.sp,
                            color = TextGray
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.testTag(TestTags.STATS_PROGRESS_COMPARE),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val comparisonData = getComparisonData(weeklyComparison)

                            if (comparisonData.icon != null) {
                                Icon(
                                    imageVector = comparisonData.icon,
                                    contentDescription = null,
                                    tint = comparisonData.color,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag(TestTags.STATS_PROGRESS_ICON)
                                        .semantics { colorProperty = comparisonData.color }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = comparisonData.text,
                                fontSize = 28.sp,
                                color = comparisonData.color,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .testTag(TestTags.STATS_PROGRESS_NUMBER)
                                    .semantics { colorProperty = comparisonData.color }
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
                                        fill = Fill(RoyalBlue),
                                        thickness = 12.dp,
                                        shape = CircleShape
                                    )
                                )
                            ),
                            bottomAxis = HorizontalAxis.rememberBottom(
                                valueFormatter = { _, value, _ ->
                                    listOf("M", "T", "W", "T", "F", "S", "S")[value.toInt() % 7]
                                },
                                guideline = null,
                                label = rememberAxisLabelComponent(
                                    style = TextStyle(color = TextGray, fontSize = 12.sp)
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
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillMasterySection(
    masteryList: List<MasteryData>,
    isExpanded: Boolean,
    onViewAllClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.stats_skill_mastery),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(R.string.stats_skill_mastery_info_title),
                    tint = TextGray,
                    modifier = Modifier
                        .size(18.dp)
                        .testTag(TestTags.STATS_SKILL_MASTERY_DESCRIPTION)
                        .clickable { onInfoClick() }
                )
            }
            TextButton(
                onClick = onViewAllClick,
                modifier = Modifier.testTag(TestTags.STATS_SKILL_MASTERY_VIEW_ALL),
            ) {
                val buttonText = if (isExpanded) {
                    stringResource(R.string.stats_skill_mastery_show_less)
                } else {
                    stringResource(R.string.stats_skill_mastery_view_all)
                }
                Text(text = buttonText, color = RoyalBlue, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.animateContentSize()) {
            AnimatedContent(
                targetState = isExpanded,
                label = "SkillMasteryTransition"
            ) { expanded ->
                val displayList = if (expanded) masteryList else masteryList.take(2)

                if (expanded) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        displayList.forEach { data ->
                            SkillCard(data)
                        }
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(end = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(displayList) { data ->
                            SkillCard(data)
                        }
                    }
                }
            }
        }
    }
}

data class MasteryData(val title: String, val percentage: Int, @androidx.annotation.StringRes val levelRes: Int, val color: Color)

@Composable
fun SkillCard(data: MasteryData) {
    Card(
        modifier = Modifier.size(width = 160.dp, height = 180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = DeepBlue)
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
                    trackColor = SurfaceDark,
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
                text = stringResource(data.levelRes),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray
            )
        }
    }
}

@Composable
fun AtAGlanceSection(
    flashcardsViewed: String,
    currentStreak: String,
    studyTime: String,
    masteredDecks: String
) {
    Column {
        Text(
            text = stringResource(R.string.stats_at_glance),
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
                    value = flashcardsViewed,
                    label = stringResource(R.string.stats_flashcards_viewed),
                    containerColor = SlateBlue,
                    iconColor = RoyalBlue,
                    testTag = TestTags.STATS_FLASHCARDS_VIEWED
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.LocalFireDepartment,
                    value = currentStreak,
                    label = stringResource(R.string.stats_current_streak),
                    containerColor = DeepBrown,
                    iconColor = WarningOrange,
                    testTag = TestTags.STATS_CURRENT_STREAK
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.AccessTime,
                    value = studyTime,
                    label = stringResource(R.string.stats_study_time),
                    containerColor = Indigo,
                    iconColor = SoftPurple,
                    testTag = TestTags.STATS_STUDY_TIME
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.CheckCircle,
                    value = masteredDecks,
                    label = stringResource(R.string.stats_mastered_decks),
                    containerColor = DeepGreen,
                    iconColor = SuccessGreen,
                    testTag = TestTags.STATS_MASTERED_DECKS
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
    iconColor: Color,
    testTag: String = ""
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .testTag(testTag),
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
                    color = TextGray
                )
            }
        }
    }
}

private fun getWeeklyDateRange(): String {
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
    val startDate = calendar.time
    calendar.add(Calendar.DAY_OF_WEEK, 6)
    val endDate = calendar.time
    val sdf = SimpleDateFormat("MMM d", Locale.getDefault())
    return "${sdf.format(startDate)} - ${sdf.format(endDate)}"
}

private data class ComparisonData(val color: Color, val icon: ImageVector?, val text: String)

private fun getComparisonData(weeklyComparison: Int): ComparisonData {
    val color = when {
        weeklyComparison > 0 -> SuccessGreen
        weeklyComparison < 0 -> ErrorRed
        else -> Color.White
    }
    val icon = when {
        weeklyComparison > 0 -> Icons.AutoMirrored.Filled.TrendingUp
        weeklyComparison < 0 -> Icons.AutoMirrored.Filled.TrendingDown
        else -> null
    }
    val text = when {
        weeklyComparison > 0 -> "+$weeklyComparison%"
        weeklyComparison < 0 -> "$weeklyComparison%"
        else -> "0%"
    }
    return ComparisonData(color, icon, text)
}

@Composable
private fun StatsInfoDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
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
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.stats_info_dialog_confirm),
                    color = RoyalBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = SurfaceDark,
        shape = RoundedCornerShape(28.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun StatsScreenPreview() {
    Flashcards2Theme {
        StatsScreen(
            uiState = UiState.Success(
                StatsUiState(
                    weeklyActivity = listOf(10, 20, 15, 30, 25, 40, 35),
                    skillMastery = listOf(
                        MasteryData("Language", 85, R.string.mastery_level_veteran, RoyalBlue),
                        MasteryData("UI/UX", 60, R.string.mastery_level_sophomore, WarningOrange)
                    ),
                    flashcardsViewed = "1,234",
                    currentStreak = "7",
                    studyTime = "12h 30m",
                    masteredDecks = "92%",
                    weeklyComparison = 12,
                    isSkillsExpanded = false,
                    infoDialogContent = null
                )
            ),
            promoDeck = Deck(1, "Kotlin", "Kotlin Fundamentals", isSelected = false),
            showSuggestions = true,
            onViewAllSkillsClicked = {},
            onInfoClick = { _, _ -> },
            onDismissInfoDialog = {},
            onRetry = {},
            onPromoClick = {}
        )
    }
}
