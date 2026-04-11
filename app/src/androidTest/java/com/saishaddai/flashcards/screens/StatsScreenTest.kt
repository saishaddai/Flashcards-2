package com.saishaddai.flashcards.screens

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class StatsScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testStatsScreen_initState_checkElementsDisplayed() {
        composeRule.setContent {
            StatsScreen()
        }
    }

    @Test
    fun testStatsScreen_onScrollableContent_checkElementsDisplayed() {
    }

    @Test
    fun testStatsScreen_onBackClick_checkNavigationCalled() {
    }

    @Test
    fun testStatsScreen_onPromoClick_checkNavigationCalled() {
    }

    @Test
    fun testStatsScreen_onSeeAllClick_showsAllDecksInfoCards() {
    }

    @Test
    fun testStatsScreen_onShareClick_triggersCallBack() {
    }


}