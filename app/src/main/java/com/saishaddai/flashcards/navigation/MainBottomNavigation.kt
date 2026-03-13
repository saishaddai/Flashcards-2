package com.saishaddai.flashcards.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.routes.Routes

@Composable
fun MainBottomNavigation(
    currentRoute: Any?,
    onLearnClick: () -> Unit,
    onInstructionsClick: () -> Unit,
    onStatsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    BottomAppBar(
        containerColor = Color(0xFF1A1A2E),
    ) {
        BottomNavigationItem(
            text = stringResource(R.string.decks_bottom_nav_learn),
            icon = Icons.Default.School,
            selected = currentRoute == Routes.DeckList,
            onClick = onLearnClick
        )
        BottomNavigationItem(
            text = stringResource(R.string.decks_bottom_nav_instructions),
            icon = Icons.Default.Info,
            selected = currentRoute == Routes.Instructions,
            onClick = onInstructionsClick
        )
        BottomNavigationItem(
            text = stringResource(R.string.decks_bottom_nav_stats),
            icon = Icons.Default.BarChart,
            selected = currentRoute == Routes.Stats,
            onClick = onStatsClick
        )
        BottomNavigationItem(
            text = stringResource(R.string.decks_bottom_nav_settings),
            icon = Icons.Default.Settings,
            selected = false, // TODO: Add Settings route
            onClick = onSettingsClick
        )
    }
}

@Composable
fun RowScope.BottomNavigationItem(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        label = { Text(text, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
        icon = { Icon(icon, contentDescription = text) }
    )
}
