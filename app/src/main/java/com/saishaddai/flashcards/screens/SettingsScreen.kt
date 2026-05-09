package com.saishaddai.flashcards.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.repository.UserSettings
import com.saishaddai.flashcards.screens.commons.BlueButton
import com.saishaddai.flashcards.screens.commons.FullLoader
import com.saishaddai.flashcards.screens.commons.Header
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import com.saishaddai.flashcards.utils.TestTags
import com.saishaddai.flashcards.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

const val DEFAULT_FLASHCARDS_PER_SESSION = 20
const val DEFAULT_DAILY_GOAL = 50

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()

    SettingsScreenContent(
        isLoading = isLoading,
        userSettings = userSettings,
        onRestartMasteryClicked = viewModel::onRestartMasteryClicked,
        onPreferredStudyTimeChanged = viewModel::onPreferredStudyTimeChanged,
        onFlashcardsPerSessionChanged = viewModel::onFlashcardsPerSessionChanged,
        onDailyStudyGoalChanged = viewModel::onDailyStudyGoalChanged,
        onQuickStartChanged = viewModel::onQuickStartChanged,
        onShowAnswersChanged = viewModel::onShowAnswersChanged,
        onShowSuggestionsChanged = viewModel::onShowSuggestionsChanged,
        onStudyRemindersChanged = viewModel::onStudyRemindersChanged,
        onNotificationSoundChanged = viewModel::onNotificationSoundChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    isLoading: Boolean,
    userSettings: UserSettings?,
    onRestartMasteryClicked: () -> Unit,
    onPreferredStudyTimeChanged: (Int, Int) -> Unit,
    onFlashcardsPerSessionChanged: (Int) -> Unit = {},
    onDailyStudyGoalChanged: (Int) -> Unit = {},
    onQuickStartChanged: (Boolean) -> Unit = {},
    onShowAnswersChanged: (Boolean) -> Unit = {},
    onShowSuggestionsChanged: (Boolean) -> Unit = {},
    onStudyRemindersChanged: (Boolean) -> Unit = {},
    onNotificationSoundChanged: (Boolean) -> Unit = {}
) {
    val showRestartDialog = rememberSaveable { mutableStateOf(false) }
    val showTimePicker = rememberSaveable { mutableStateOf(false) }

    val localFlashcardsPerSession = remember(userSettings?.flashcardsPerSession) {
        mutableStateOf(userSettings?.flashcardsPerSession?.toFloat() ?: DEFAULT_FLASHCARDS_PER_SESSION.toFloat())
    }
    val localDailyGoal = remember(userSettings?.dailyStudyGoal) {
        mutableStateOf(userSettings?.dailyStudyGoal?.toFloat() ?: DEFAULT_DAILY_GOAL.toFloat())
    }

    if (isLoading || userSettings == null) {
        FullLoader(message = null)
    } else {
        if (showRestartDialog.value) {
            RestartMasteryDialog(
                onDismiss = { showRestartDialog.value = false },
                onConfirm = {
                    onRestartMasteryClicked()
                    showRestartDialog.value = false
                }
            )
        }

        if (showTimePicker.value) {
            TimePickerDialogWrapper(
                onDismiss = { showTimePicker.value = false },
                onConfirm = { hour, minute ->
                    onPreferredStudyTimeChanged(hour, minute)
                    showTimePicker.value = false
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A2E))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Header(
                headText = stringResource(R.string.settings_head_title),
                titleText = stringResource(R.string.settings_title),
                subtitleText = stringResource(R.string.settings_subtitle)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // STUDY SESSION
            SectionHeader(title = stringResource(R.string.settings_section_study_session))
            SliderSetting(
                icon = Icons.Default.School,
                title = stringResource(R.string.settings_flashcards_per_session),
                value = localFlashcardsPerSession.value.toInt().toString(),
                currentValue = localFlashcardsPerSession.value,
                onValueChange = { localFlashcardsPerSession.value = it },
                onValueChangeFinished = { onFlashcardsPerSessionChanged(localFlashcardsPerSession.value.toInt()) },
                range = 5f..50f,
                minLabel = stringResource(R.string.settings_flashcards_per_session_min),
                maxLabel = stringResource(R.string.settings_flashcards_per_session_max),
                modifier = Modifier.testTag("slider_session")
            )

            SliderSetting(
                icon = Icons.Default.Flag,
                title = stringResource(R.string.settings_daily_goal),
                value = stringResource(R.string.settings_daily_goal_value, localDailyGoal.value.toInt()),
                currentValue = localDailyGoal.value,
                onValueChange = { localDailyGoal.value = it },
                onValueChangeFinished = { onDailyStudyGoalChanged(localDailyGoal.value.toInt()) },
                range = 10f..100f,
                minLabel = stringResource(R.string.settings_daily_goal_min),
                maxLabel = stringResource(R.string.settings_daily_goal_max)
            )

            val resetEnabled by remember(userSettings) {
                derivedStateOf {
                    userSettings.flashcardsPerSession != DEFAULT_FLASHCARDS_PER_SESSION || 
                    userSettings.dailyStudyGoal != DEFAULT_DAILY_GOAL
                }
            }
            
            BlueButton(
                icon = Icons.Default.Replay,
                text = stringResource(R.string.settings_flashcards_per_session_reset),
                enabled = resetEnabled,
                onClick = {
                    onFlashcardsPerSessionChanged(DEFAULT_FLASHCARDS_PER_SESSION)
                    onDailyStudyGoalChanged(DEFAULT_DAILY_GOAL)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // PERSONALIZATION
            SectionHeader(title = stringResource(R.string.settings_section_personalization))
            SwitchSetting(
                icon = Icons.Default.Add,
                title = stringResource(R.string.settings_quick_start),
                description = stringResource(R.string.settings_quick_start_text),
                checked = userSettings.quickStart,
                testTag = TestTags.SETTINGS_QUICK_START,
                onCheckedChange = onQuickStartChanged
            )

            SwitchSetting(
                icon = Icons.Default.Add,
                title = stringResource(R.string.settings_show_answers),
                description = stringResource(R.string.settings_show_answers_text),
                checked = userSettings.showAnswers,
                testTag = TestTags.SETTINGS_SHOW_ANSWERS,
                onCheckedChange = onShowAnswersChanged
            )

            SwitchSetting(
                icon = Icons.Default.Add,
                title = stringResource(R.string.settings_show_suggestions),
                description = stringResource(R.string.settings_show_suggestions_text),
                checked = userSettings.showSuggestions,
                testTag = TestTags.SETTINGS_SHOW_SUGGESTIONS,
                onCheckedChange = onShowSuggestionsChanged
            )

            Spacer(modifier = Modifier.height(32.dp))

            // NOTIFICATIONS
            SectionHeader(title = stringResource(R.string.settings_section_notifications))
            SwitchSetting(
                icon = Icons.Default.Notifications,
                title = stringResource(R.string.settings_daily_reminders),
                description = stringResource(R.string.settings_daily_reminders_description),
                checked = userSettings.studyReminders,
                testTag = TestTags.SETTINGS_STUDY_REMINDERS,
                onCheckedChange = onStudyRemindersChanged
            )

            ActionSetting(
                icon = Icons.Default.AccessTime,
                title = stringResource(R.string.settings_preferred_study_time),
                description = stringResource(R.string.settings_preferred_study_time_description),
                actionLabel = userSettings.preferredStudyTime,
                onClick = { showTimePicker.value = true }
            )

            SwitchSetting(
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                title = stringResource(R.string.settings_notification_sound),
                description = stringResource(R.string.settings_notification_sound_description),
                checked = userSettings.notificationSound,
                testTag = TestTags.SETTINGS_NOTIFICATION_SOUND,
                onCheckedChange = onNotificationSoundChanged
            )

            Spacer(modifier = Modifier.height(32.dp))

            // SYSTEM
            SectionHeader(title = stringResource(R.string.settings_section_system))
            RestartMasteryButton(onClick = { showRestartDialog.value = true })
            Text(
                text = stringResource(R.string.settings_system_restart_description),
                color = Color(0xFFB0B0B0),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Footer
            SettingsFooter()
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = RoyalBlue,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun SliderSetting(
    icon: ImageVector,
    title: String,
    value: String,
    currentValue: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onValueChangeFinished: () -> Unit = {},
    range: ClosedFloatingPointRange<Float>,
    minLabel: String,
    maxLabel: String
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Text(text = value, color = RoyalBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Slider(
            value = currentValue,
            onValueChange = onValueChange,
            onValueChangeFinished = onValueChangeFinished,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = RoyalBlue,
                activeTrackColor = RoyalBlue,
                inactiveTrackColor = Color(0xFF2C2C4E)
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = minLabel, color = Color(0xFFB0B0B0), fontSize = 12.sp)
            Text(text = maxLabel, color = Color(0xFFB0B0B0), fontSize = 12.sp)
        }
    }
}

@Composable
fun SwitchSetting(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    testTag: String,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFB0B0B0), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = description, color = Color(0xFFB0B0B0), fontSize = 12.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag + "_switch"),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = RoyalBlue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF2C2C4E)
            )
        )
    }
}

@Composable
fun ActionSetting(
    icon: ImageVector,
    title: String,
    description: String,
    actionLabel: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFB0B0B0), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = description, color = Color(0xFFB0B0B0), fontSize = 12.sp)
            }
        }
        Box(
            modifier = Modifier
                .background(Color(0xFF2C2C4E), RoundedCornerShape(50))
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = actionLabel, color = RoyalBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun RestartMasteryButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF251A24)) // Dark Reddish
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFF06292))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.settings_system_restart),
                color = Color(0xFFF06292),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun SettingsFooter() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null, tint = Color(0xFF4D4D66), modifier = Modifier.size(20.dp))
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF4D4D66), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.app_version), color = Color(0xFFB0B0B0), fontSize = 14.sp)
        Text(text = stringResource(R.string.last_updated), color = Color(0xFFB0B0B0), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.designed_for), color = Color(0xFF4D4D66), fontSize = 12.sp)
    }
}

@Composable
fun RestartMasteryDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.testTag("restart_dialog"),
        title = {
            Text(
                text = stringResource(R.string.settings_system_restart),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        text = {
            Text(
                text = stringResource(R.string.settings_system_restart_description),
                color = Color(0xFFB0B0B0)
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.settings_restart_dialog_confirm),
                    color = Color(0xFFF06292),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.settings_restart_dialog_cancel),
                    color = RoyalBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = Color(0xFF2C2C4E),
        shape = RoundedCornerShape(28.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialogWrapper(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = RoundedCornerShape(28.dp),
                    color = Color(0xFF2C2C4E)
                ),
            color = Color(0xFF2C2C4E)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = stringResource(R.string.settings_preferred_study_time),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                TimePicker(
                    state = timePickerState
                )
                Row(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(
                            text = stringResource(R.string.settings_restart_dialog_cancel),
                            color = RoyalBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { onConfirm(timePickerState.hour, timePickerState.minute) }) {
                        Text(
                            text = stringResource(R.string.settings_restart_dialog_confirm),
                            color = RoyalBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    Flashcards2Theme {
        SettingsScreenContent(
            isLoading = false,
            userSettings = UserSettings(
                flashcardsPerSession = 20,
                dailyStudyGoal = 50,
                isDarkMode = true,
                studyReminders = true,
                notificationSound = false,
                preferredStudyTime = "09:00 PM",
                quickStart = true,
                showAnswers = true,
                showSuggestions = true
            ),
            onRestartMasteryClicked = {},
            onPreferredStudyTimeChanged = { _, _ -> }
        )
    }
}
