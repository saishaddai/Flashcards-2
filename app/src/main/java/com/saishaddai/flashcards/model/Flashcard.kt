package com.saishaddai.flashcards.model

import com.saishaddai.flashcards.screens.Flashcard

data class Flashcard(val deckId: Int,
                     val id: Int,
                     val question: String,
                     val answer: String,
                     val discovered: Boolean = false
)

val flashcards = listOf(
    Flashcard(1, 1, "1: What is the capital of France?", "Paris", false),
    Flashcard(1, 2, "1: What is the largest planet in our solar system?", "Jupiter", false),
    Flashcard(1, 3, "1: What is the smallest country in the world?", "Vatican City", false),
    Flashcard(1, 4, "1: What is the highest mountain in the world?", "Mount Everest", false),

    Flashcard(2, 5, "2: What is the capital of France?", "Paris", false),
    Flashcard(2, 6, "2: What is the largest planet in our solar system?", "Jupiter", false),
    Flashcard(2, 7, "2: What is the smallest country in the world?", "Vatican City", false),

    Flashcard(3, 8, "3: What is the capital of France?", "Paris", false),
    Flashcard(3, 9, "3: What is the largest planet in our solar system?", "Jupiter", false),
    Flashcard(3, 10, "3: What is the smallest country in the world?", "Vatican City", false),
    Flashcard(3, 11, "3: What is the highest mountain in the world?", "Mount Everest", false),

    Flashcard(4, 12, "4: What is the capital of France?", "Paris", false),
    Flashcard(4, 13, "4: What is the largest planet in our solar system?", "Jupiter", false),
    Flashcard(4, 14, "4: What is the smallest country in the world?", "Vatican City", false),
    Flashcard(4, 15, "4: What is the highest mountain in the world?", "Mount Everest", false),

)
