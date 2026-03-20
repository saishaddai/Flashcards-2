package com.saishaddai.flashcards.model.fcdata

import com.saishaddai.flashcards.model.Flashcard


val kotlinCards = listOf(
    Flashcard(
        3,
        1,
        "What is a 'val' in Kotlin?",
        "A read-only variable whose value cannot be changed once assigned."
    ),
    Flashcard(
        3,
        2,
        "What is a 'var' in Kotlin?",
        "A mutable variable whose value can be reassigned."
    ),
    Flashcard(
        3,
        3,
        "What are extension functions?",
        "Functions that add new functionality to an existing class without modifying it."
    ),
    Flashcard(
        3,
        4,
        "What is null safety in Kotlin?",
        "A system designed to eliminate NullPointerExceptions by distinguishing between nullable and non-nullable types."
    ),
    Flashcard(
        3,
        5,
        "What is a class in Kotlin?",
        "A blueprint for creating objects, encapsulating data and behavior."
    ),
    Flashcard(
        3,
        6,
        "What is the difference between primary and secondary constructors in Kotlin?",
        "Primary is declared in the class header; secondary is defined inside the class body using the 'constructor' keyword."
    ),
    Flashcard(
        3,
        7,
        "What is a data class in Kotlin? How it is different from Java's model class?",
        "A class that automatically generates methods like equals(), hashCode(), and toString() for data-holding objects."
    ),
    Flashcard(
        3,
        8,
        "What is a sealed class and sealed interface in Kotlin?",
        "Restricted hierarchies where all subclasses are known at compile time, providing type safety in 'when' expressions."
    ),
    Flashcard(
        3,
        9,
        "What is an abstract class in Kotlin?",
        "A class that cannot be instantiated and can contain abstract members to be implemented by subclasses."
    ),
    Flashcard(
        3,
        10,
        "What is an interface in Kotlin?",
        "A contract that defines functions and properties that classes can implement, possibly providing default implementations."
    ),
    Flashcard(
        3,
        11,
        "Do we need open keyword for interface and abstract class as well?",
        "No, they are open by default since they are intended to be implemented or extended."
    ),
    Flashcard(
        3,
        12,
        "What is the difference between an abstract class and an interface in Kotlin?",
        "Abstract classes can store state (properties with backing fields), whereas interfaces cannot."
    ),
    Flashcard(
        3,
        13,
        "What is inheritance in Kotlin?",
        "A mechanism where a class (subclass) derives from another class (superclass) using the ':' operator."
    ),
    Flashcard(
        3,
        14,
        "What is the purpose of the open keyword in Kotlin?",
        "In Kotlin, classes and members are final by default; 'open' allows them to be inherited or overridden."
    ),
    Flashcard(
        3,
        15,
        "What is delegation in Kotlin?",
        "A design pattern where an object handles a request by delegating it to a second object, often used with the 'by' keyword."
    ),
    Flashcard(
        3,
        16,
        "What is object declaration in Kotlin? Is it thread safe?",
        "A way to define a singleton object. It is thread-safe and lazily initialized."
    ),
    Flashcard(
        3,
        17,
        "What is a companion object in Kotlin?",
        "An object declared within a class that can be called like a static method in Java, sharing the class's scope."
    )
)
