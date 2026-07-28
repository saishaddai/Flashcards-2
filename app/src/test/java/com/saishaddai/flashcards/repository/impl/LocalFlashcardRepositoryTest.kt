package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.data.assets.FlashcardAssetDataSource
import com.saishaddai.flashcards.data.local.FlashcardDao
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LocalFlashcardRepositoryTest {

    private lateinit var repository: LocalFlashcardRepository
    private val flashcardDao: FlashcardDao = mock()
    private val flashcardAssetDataSource: FlashcardAssetDataSource = mock()

    @Before
    fun setUp() {
        repository = LocalFlashcardRepository(flashcardAssetDataSource, flashcardDao)
    }

    @Test
    fun `getData returns flashcards from DAO when database is not empty`() = runTest {
        // Given
        val deckType = DeckType.KOTLIN
        val expectedCards = listOf(
            Flashcard(deckType.id, 1, "Q1", "A1"),
            Flashcard(deckType.id, 2, "Q2", "A2")
        )
        whenever(flashcardDao.getTotalFlashcardCount()).thenReturn(10)
        whenever(flashcardDao.getFlashcardsForDeck(deckType.id, 5)).thenReturn(expectedCards)

        // When
        val result = repository.getData(deckType, 5)

        // Then
        assertEquals(expectedCards, result)
        verify(flashcardDao).getFlashcardsForDeck(deckType.id, 5)
        verify(flashcardDao, never()).insertAll(any())
    }

    @Test
    fun `getDataCount returns count from DAO when database is not empty`() = runTest {
        // Given
        val deckType = DeckType.COMPOSE
        whenever(flashcardDao.getTotalFlashcardCount()).thenReturn(10)
        whenever(flashcardDao.getFlashcardCountForDeck(deckType.id)).thenReturn(42)

        // When
        val result = repository.getDataCount(deckType)

        // Then
        assertEquals(42, result)
        verify(flashcardDao).getFlashcardCountForDeck(deckType.id)
    }

    @Test
    fun `ensureDataLoaded calls asset data source when database is empty`() = runTest {
        // Given
        whenever(flashcardDao.getTotalFlashcardCount()).thenReturn(0)
        whenever(flashcardAssetDataSource.loadFlashcardsForDeck(any())).thenReturn(emptyList())
        
        // When
        try {
            repository.getDataCount(DeckType.OOP)
        } catch (e: Exception) {
            // Expected if no cards loaded
        }

        // Then
        verify(flashcardDao).getTotalFlashcardCount()
        verify(flashcardAssetDataSource).loadFlashcardsForDeck(DeckType.OOP)
    }
}
