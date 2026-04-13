package com.saishaddai.flashcards.model.fcdata

import com.saishaddai.flashcards.model.Flashcard


val composeCards = listOf(
    Flashcard(6,  93, "What is Jetpack Compose?", "Android's modern toolkit for building native UI using a declarative approach."),
    Flashcard(6,  94, "What is a Composable function?", "A function annotated with @Composable that defines a part of the UI."),
    Flashcard(6,  95, "What is State in Compose?", "An observable value that triggers a recomposition when it changes."),
    Flashcard(6,  96, "What is Recomposition?", "The process of calling Composable functions again when their inputs change to update the UI."),
    Flashcard(6,  97, "What is 'remember' in Compose?", "A function used to store an object in memory during recomposition."),
    Flashcard(6,  98, "What is a 'Side-effect' in Compose?", "A change to the state of the app that happens outside the scope of a Composable function."),
    Flashcard(6,  99, "What is a Modifier in Compose?", "An ordered collection of elements that decorate or augment a Composable."),
    Flashcard(6,  100, "What is a 'Scaffold' in Compose?", "A Composable that provides a standard layout structure for common UI components."),
    Flashcard(6,  101, "What is 'CompositionLocal'?", "A way to pass data through the Composition tree without explicitly passing it as a parameter."),
    Flashcard(6,  102, "What is 'derivedStateOf'?", "A function used to create a State that depends on other State objects, optimizing recomposition."),
    Flashcard(6,  103, "What is a 'LazyColumn'?", "A vertically scrolling list that only composes and lays out currently visible items."),
    Flashcard(6,  104, "What is 'LaunchedEffect'?", "A Composable used to run side effects in the scope of a Composable, cancelled when it leaves composition."),
    Flashcard(6,  105, "What is 'rememberUpdatedState'?", "A way to reference a value in an effect that shouldn't restart when the value changes.")
)
