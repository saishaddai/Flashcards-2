package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.model.sessions
import com.saishaddai.flashcards.repository.DeckRepository
import com.saishaddai.flashcards.repository.FlashcardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JSONDeckRepository(
    private val flashcardRepository: FlashcardRepository<Flashcard>
) : DeckRepository<Deck> {

    override suspend fun getData(): List<Deck> = withContext(Dispatchers.IO) {
        decks.onEach {
            if (it.cardCount == 0) {
                it.cardCount = flashcardRepository.getDataCount(it.id)
            }
        }
    }

    val decks = listOf(
        Deck(DeckType.OOP.id, "OOP", "Object-Oriented Programming", isSelected = true),
        Deck(DeckType.ANDROID_CORE.id, "Android Core", "Android Core Technologies"),
        Deck(DeckType.KOTLIN.id, "Kotlin", "Kotlin Programming Language"),
        Deck(DeckType.KOTLIN_MP.id, "Kotlin MP", "Kotlin Multiplatform"),
        Deck(DeckType.SECURITY.id, "Security", "Android Security"),
        Deck(DeckType.COMPOSE.id, "Compose", "Jetpack Compose UI"),
        Deck(DeckType.DATABASES.id, "Databases", "Android Databases"),
        Deck(DeckType.DAGGER_HILT.id, "Dagger/Hilt", "Android Dependency Injection"),
        Deck(DeckType.MATERIAL_3.id, "Material 3", "Android Material Design"),
        Deck(DeckType.NAVIGATION.id, "Navigation", "Android Navigation Component"),
        Deck(DeckType.JETPACK.id, "Jetpack", "Android Jetpack"),
        Deck(DeckType.TESTING.id, "Testing", "Android Testing"),
        Deck(DeckType.GRADLE.id, "Gradle", "Android Gradle"),
        Deck(DeckType.ANDROID_OPS.id, "Android OPS", "Continuous Integration/Deployment"),
        Deck(DeckType.LIBRARIES.id, "Libraries", "Android Libraries"),
        Deck(DeckType.DESIGN_PATTERNS.id, "Design Patterns", "Android Design Patterns"),
        Deck(DeckType.COROUTINES.id, "Coroutines", "Android Coroutines"),
        Deck(DeckType.FIREBASE.id, "Firebase", "Firebase Integration"),
        Deck(DeckType.GRAPHQL.id, "GraphQL", "GraphQL Integration")
    ).map {
        it.apply {
            mastery = sessions.find { session -> session.deckId == it.id }?.currentXP ?: 0
        }
    }

}