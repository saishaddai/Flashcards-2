package com.saishaddai.flashcards.screens.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import com.saishaddai.flashcards.utils.TestTags

@Composable
fun FullLoader(
    message: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .testTag(TestTags.FULL_LOADER),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .height(150.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2C2C4E)
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.f2_card),
                contentDescription = null,
                modifier = Modifier.size(156.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        CircularProgressIndicator(color = RoyalBlue)
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = message ?: stringResource(R.string.loading),
            color = Color.White,
            fontSize = 24.sp
        )
    }
}

@Preview
@Composable
fun PreviewFullLoader() {
    FullLoader(null)
}
