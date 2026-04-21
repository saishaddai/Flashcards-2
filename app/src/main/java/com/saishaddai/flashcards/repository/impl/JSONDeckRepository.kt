package com.saishaddai.flashcards.repository.impl

import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.DeckType.*
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
        Deck(OOP.id, "OOP", "Object-Oriented Programming", isSelected = true),
        Deck(ANDROID_CORE.id, "Android Core", "Android Core Technologies"),
        Deck(KOTLIN.id, "Kotlin", "Kotlin Programming Language"),
        Deck(KOTLIN_MP.id, "Kotlin MP", "Kotlin Multiplatform"),
        Deck(SECURITY.id, "Security", "Android Security"),
        Deck(COMPOSE.id, "Compose", "Jetpack Compose UI"),
        Deck(DATABASES.id, "Databases", "Android Databases"),
        Deck(DAGGER_HILT.id, "Dagger/Hilt", "Android Dependency Injection"),
        Deck(MATERIAL_3.id, "Material 3", "Android Material Design"),
        Deck(NAVIGATION.id, "Navigation", "Android Navigation Component"),
        Deck(JETPACK.id, "Jetpack", "Android Jetpack"),
        Deck(TESTING.id, "Testing", "Android Testing"),
        Deck(GRADLE.id, "Gradle", "Android Gradle"),
        Deck(ANDROID_OPS.id, "Android OPS", "Continuous Integration/Deployment"),
        Deck(LIBRARIES.id, "Libraries", "Android Libraries"),
        Deck(DESIGN_PATTERNS.id, "Design Patterns", "Android Design Patterns"),
        Deck(COROUTINES.id, "Coroutines", "Android Coroutines"),
        Deck(FIREBASE.id, "Firebase", "Firebase Integration"),
        Deck(GRAPHQL.id, "GraphQL", "GraphQL Integration")
    ).map {
        it.apply {
            mastery = sessions.find { session -> session.deckId == it.id }?.currentXP ?: 0
        }
    }

}