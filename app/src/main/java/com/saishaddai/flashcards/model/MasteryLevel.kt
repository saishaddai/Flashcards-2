package com.saishaddai.flashcards.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.saishaddai.flashcards.R
import com.saishaddai.flashcards.ui.theme.*

enum class MasteryLevel(
    val minProgress: Int,
    @StringRes val nameRes: Int,
    val color: Color
) {
    NOT_STARTED(0, R.string.mastery_level_not_started, TextGray),
    NOVICE(1, R.string.mastery_level_novice, TextGray),
    SOPHOMORE(26, R.string.mastery_level_sophomore, WarningOrange),
    EXPERIENCED(51, R.string.mastery_level_experienced, SuccessGreen),
    VETERAN(76, R.string.mastery_level_veteran, RoyalBlue),
    MASTERED(100, R.string.mastery_level_mastered, Gold);

    companion object {
        fun fromProgress(progress: Int): MasteryLevel {
            return entries.filter { progress >= it.minProgress }.maxByOrNull { it.minProgress } ?: NOT_STARTED
        }
    }
}
