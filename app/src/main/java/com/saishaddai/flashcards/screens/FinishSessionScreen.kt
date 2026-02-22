package com.saishaddai.flashcards.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saishaddai.flashcards.ui.theme.Flashcards2Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishSessionScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SESSION SUMMARY",
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Implement close */ }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement share */ }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            TrophyIcon()
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "All Done!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Great job on your Android prep today.",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoCard(
                    title = "REVIEWED",
                    value = "20",
                    unit = "cards",
                    icon = Icons.Default.Style
                )
                InfoCard(
                    title = "DURATION",
                    value = "12",
                    unit = "mins",
                    icon = Icons.Default.Timer
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            DailyGoalReached()
            Spacer(modifier = Modifier.weight(1f))
            BackToTopicsButton(modifier = Modifier.fillMaxWidth())
            ReviewSessionButton(modifier = Modifier.fillMaxWidth())
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
            .background(Color(0xFF4D8EFF))
    ) {
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = "Trophy",
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
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF4D8EFF))
            Text(text = title, fontSize = 12.sp, color = Color(0xFF4D8EFF))
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
fun DailyGoalReached() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0x3300BFA5))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
            contentDescription = null,
            tint = Color(0xFF00BFA5)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Daily goal reached! +50 XP", color = Color(0xFF00BFA5))
    }
}

@Composable
fun BackToTopicsButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { /*TODO*/ },
        modifier = modifier,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4D8EFF))
    ) {
        Icon(Icons.Default.GridView, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Back to Topics")
    }
}

@Composable
fun ReviewSessionButton(modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = modifier,
        shape = RoundedCornerShape(50),
        border = BorderStroke(2.dp, Color(0xFF4D8EFF))
    ) {
        Icon(Icons.Default.History, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Review Session", color = Color.White)
    }
}


@Preview(showBackground = true)
@Composable
fun FinishSessionScreenPreview() {
    Flashcards2Theme {
        FinishSessionScreen()
    }
}
