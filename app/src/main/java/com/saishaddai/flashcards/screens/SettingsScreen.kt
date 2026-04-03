package com.saishaddai.flashcards.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.screens.commons.BlueButton
import com.saishaddai.flashcards.screens.commons.Header
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.ui.theme.RoyalBlue

const val FLASHCARDS_PER_SESSION = 20f
const val DAILY_GOAL = 50f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
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
        var flashcardsPerSession by remember { mutableFloatStateOf(FLASHCARDS_PER_SESSION) }
        SliderSetting(
            icon = Icons.Default.School,
            title = stringResource(R.string.settings_flashcards_per_session),
            value = flashcardsPerSession.toInt().toString(),
            currentValue = flashcardsPerSession,
            onValueChange = { flashcardsPerSession = it },
            range = 5f..50f,
            minLabel = stringResource(R.string.settings_flashcards_per_session_min),
            maxLabel = stringResource(R.string.settings_flashcards_per_session_max)
        )

        var dailyStudyGoal by remember { mutableFloatStateOf(DAILY_GOAL) }
        SliderSetting(
            icon = Icons.Default.Flag,
            title = stringResource(R.string.settings_daily_goal),
            value = stringResource(R.string.settings_daily_goal_value, dailyStudyGoal.toInt()),
                //"${dailyStudyGoal.toInt()} cards",
            currentValue = dailyStudyGoal,
            onValueChange = { dailyStudyGoal = it },
            range = 10f..100f,
            minLabel = stringResource(R.string.settings_daily_goal_min),
            maxLabel = stringResource(R.string.settings_daily_goal_max)
        )

        val resetEnabled by remember {
            derivedStateOf {
                flashcardsPerSession != FLASHCARDS_PER_SESSION || dailyStudyGoal != DAILY_GOAL
            }
        }
        
        BlueButton(
            icon = Icons.Default.Replay,
            text = stringResource(R.string.settings_flashcards_per_session_reset),
            enabled = resetEnabled,
            onClick = {
                flashcardsPerSession = FLASHCARDS_PER_SESSION
                dailyStudyGoal = DAILY_GOAL
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // PERSONALIZATION
        SectionHeader(title = stringResource(R.string.settings_section_personalization))
        var darkMode by remember { mutableStateOf(true) }
        SwitchSetting(
            icon = Icons.Default.DarkMode,
            title = stringResource(R.string.settings_dark_mode),
            description = stringResource(R.string.settings_dark_mode_description),
            checked = darkMode,
            onCheckedChange = { darkMode = it }
        )
        
        ActionSetting(
            icon = Icons.Default.AccessTime,
            title = stringResource(R.string.settings_preferred_study_time),
            description = stringResource(R.string.settings_preferred_study_time_description),
            actionLabel = "09:00 PM",
            onClick = { /* TODO: Time picker */ }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // NOTIFICATIONS
        SectionHeader(title = stringResource(R.string.settings_section_notifications))
        var studyReminders by remember { mutableStateOf(true) }
        SwitchSetting(
            icon = Icons.Default.Notifications,
            title = stringResource(R.string.settings_daily_reminders),
            description = stringResource(R.string.settings_daily_reminders_description),
            checked = studyReminders,
            onCheckedChange = { studyReminders = it }
        )

        var notificationSound by remember { mutableStateOf(false) }
        SwitchSetting(
            icon = Icons.AutoMirrored.Filled.VolumeUp,
            title = stringResource(R.string.settings_notification_sound),
            description = stringResource(R.string.settings_notification_sound_description),
            checked = notificationSound,
            onCheckedChange = { notificationSound = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // SYSTEM
        SectionHeader(title = stringResource(R.string.settings_section_system))
        RestartMasteryButton()
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
    range: ClosedFloatingPointRange<Float>,
    minLabel: String,
    maxLabel: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
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
    onCheckedChange: (Boolean) -> Unit
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
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
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
fun RestartMasteryButton() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO */ },
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

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    Flashcards2Theme {
        SettingsScreen()
    }
}
