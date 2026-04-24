package com.saishaddai.flashcards.screens.commons

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saishaddai.flashcards.ui.theme.RoyalBlue
import com.saishaddai.flashcards.utils.TestTags

@Composable
fun BlueButton(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .testTag(TestTags.BLUE_BUTTON),
        enabled = enabled,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = RoyalBlue,
            contentColor = Color.White,
            disabledContainerColor = RoyalBlue.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.5f)
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}

@Composable
fun TransparentButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .testTag(TestTags.TRANSPARENT_BUTTON),
        shape = RoundedCornerShape(50)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.Gray
        )
    }
}

@Preview
@Composable
fun PreviewBlueButton() {
    BlueButton(Icons.Default.Preview, text= "Blue Button", onClick = {})
}

@Preview
@Composable
fun PreviewTransparentButton() {
    TransparentButton(Icons.Default.Preview, text= "Transparent Button", onClick = {})
}