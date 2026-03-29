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
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme
import com.saishaddai.flashcards.ui.theme.RoyalBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Navigate back */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Back",
                            tint = RoyalBlue,
                            modifier = Modifier.size(20.dp)
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // STUDY SESSION
            SectionHeader(title = "STUDY SESSION")
            var flashcardsPerSession by remember { mutableFloatStateOf(20f) }
            SliderSetting(
                icon = Icons.Default.School,
                title = "Flashcards per Session",
                value = flashcardsPerSession.toInt().toString(),
                currentValue = flashcardsPerSession,
                onValueChange = { flashcardsPerSession = it },
                range = 5f..50f,
                minLabel = "5",
                maxLabel = "50"
            )

            var dailyStudyGoal by remember { mutableFloatStateOf(50f) }
            SliderSetting(
                icon = Icons.Default.Flag,
                title = "Daily Study Goal",
                value = "${dailyStudyGoal.toInt()} cards",
                currentValue = dailyStudyGoal,
                onValueChange = { dailyStudyGoal = it },
                range = 10f..100f,
                minLabel = "10",
                maxLabel = "100"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // PERSONALIZATION
            SectionHeader(title = "PERSONALIZATION")
            var darkMode by remember { mutableStateOf(true) }
            SwitchSetting(
                icon = Icons.Default.DarkMode,
                title = "Dark Mode",
                description = "Always use dark theme",
                checked = darkMode,
                onCheckedChange = { darkMode = it }
            )
            
            ActionSetting(
                icon = Icons.Default.AccessTime,
                title = "Preferred Study Time",
                description = "Best time for focused sessions",
                actionLabel = "09:00 PM",
                onClick = { /* TODO: Time picker */ }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // NOTIFICATIONS
            SectionHeader(title = "NOTIFICATIONS")
            var studyReminders by remember { mutableStateOf(true) }
            SwitchSetting(
                icon = Icons.Default.Notifications,
                title = "Daily Study Reminders",
                description = "Keep your streak alive",
                checked = studyReminders,
                onCheckedChange = { studyReminders = it }
            )

            var notificationSound by remember { mutableStateOf(false) }
            SwitchSetting(
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                title = "Notification Sound",
                description = "Play alert sound for reminders",
                checked = notificationSound,
                onCheckedChange = { notificationSound = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // SYSTEM
            SectionHeader(title = "SYSTEM")
            RestartMasteryButton()
            Text(
                text = "This action will reset all your learned cards and cannot be undone.",
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
                text = "Restart Mastery Experience",
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
        Text(text = "App Version 1.2.0", color = Color(0xFFB0B0B0), fontSize = 14.sp)
        Text(text = "Last Updated: Oct 24, 2023", color = Color(0xFFB0B0B0), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Designed for Android Developers", color = Color(0xFF4D4D66), fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    Flashcards2Theme {
        SettingsScreen()
    }
}
