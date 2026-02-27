package com.saishaddai.flashcards.model

data class Flashcard(val deckId: Int,
                     val id: Int,
                     val question: String,
                     val answer: String,
                     val discovered: Boolean = false
)

val flashcards = listOf(
    //OOP
    Flashcard(1, 1, "What is a class?", "A blueprint for creating objects", false),
    Flashcard(1, 2, "What is an object?", "An instance of a class", false),
    Flashcard(1, 3, "What is an interface?", "A contract with methods without implementations", false),
    Flashcard(1, 4, "What is an abstract class?", "A base class with partial implementations", false),
    Flashcard(1, 5, "Tell me the four pillars of OOP", "Encapsulation, Inheritance, Polymorphism, Abstraction", false),
    Flashcard(1, 6, "What is Encapsulation?", "Hide internal state of a class, control access to it", false),
    Flashcard(1, 7, "Whats is Abstraction?", "Simplify use of a class exposing only the information that matters", false),
    Flashcard(1, 8, "What is Polymorphism?", "Same method, different behavior", false),
    Flashcard(1, 9, "What is Inheritance?", "Reuse code via Parent-Child relationship", false),
    Flashcard(1, 10, "In classes relationships, what is Association?", "General link between classes", false),
    Flashcard(1, 11, "In classes relationships, what is Aggregation?", "Whole part relationship, parts can exist independently. Example: Library - Book (books can exists outside the library)", false),
    Flashcard(1, 12, "In classes relationships, what is Composition?", "String whole part relationship. Parts die with the whole. Example House - Room (rooms aare parte of a house and nowhere else)", false),
    Flashcard(1, 13, "In classes relationships, what is Dependency?", "One class uses another temporarily. Example: Car - Mechanic (car depends on mechanic for service", false),
    Flashcard(1, 14, "In classes relationships, what is Realization?", "Class implements interface. Example: Bird implements Flyable", false),
    Flashcard(1, 15, "What are Unidirectional relationships?", "One-way relationship. Example Order - Customer (order knows customer but no vice versa", false),
    Flashcard(1, 16, "What are Bidirectional relationships?", "Two-way relationship. Example Husband - Wife (both knows each other", false),
    Flashcard(1, 17, "What are the OOP Pitfalls?", "Overuse of inheritance, tight coupling, poor encapsulation, violate SOLID principles", false),
    Flashcard(1, 18, "What the S means in SOLID principles?", "Every class has only one single responsibility and no more", false),
    Flashcard(1, 19, "What the O means in SOLID principles?", "Open to extension, close to modification", false),
    Flashcard(1, 20, "What the L means in SOLID principles?", "Liskov Substitution. subclass can be substituted for its base class without affecting the application", false),
    Flashcard(1, 21, "What the I means in SOLID principles?", "Interface Segregation. An interface can't make you implement all the methods in it", false),
    Flashcard(1, 22, "What the D means in SOLID principles?", "Dependency Inversion. A class must depend on abstractions not implementations", false),

    //Android Core
    Flashcard(2, 5, "2: What is the capital of France?", "Paris", false),
    Flashcard(2, 6, "2: What is the largest planet in our solar system?", "Jupiter", false),
    Flashcard(2, 7, "2: What is the smallest country in the world?", "Vatican City", false),

    //Kotlin
    Flashcard(3, 8, "3: What is the capital of France?", "Paris", false),
    Flashcard(3, 9, "3: What is the largest planet in our solar system?", "Jupiter", false),
    Flashcard(3, 10, "3: What is the smallest country in the world?", "Vatican City", false),
    Flashcard(3, 11, "3: What is the highest mountain in the world?", "Mount Everest", false),

    //Kotlin MP
    Flashcard(4, 12, "4: What is the capital of France?", "Paris", false),
    Flashcard(4, 13, "4: What is the largest planet in our solar system?", "Jupiter", false),
    Flashcard(4, 14, "4: What is the smallest country in the world?", "Vatican City", false),
    Flashcard(4, 15, "4: What is the highest mountain in the world?", "Mount Everest", false),

)
