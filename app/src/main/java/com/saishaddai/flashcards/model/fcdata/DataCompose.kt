package com.saishaddai.flashcards.model.fcdata

import com.saishaddai.flashcards.model.Flashcard


val composeCards = listOf(
    Flashcard(6, 93, "What is Jetpack Compose?", "Android's modern toolkit for building native UI using a declarative approach."),
    Flashcard(6, 94, "What is a Composable function?", "A function annotated with @Composable that defines a part of the UI."),
    Flashcard(6, 95, "What is State in Compose?", "An observable value that triggers a recomposition when it changes."),
    Flashcard(6, 96, "What is Recomposition?", "The process of calling Composable functions again when their inputs change to update the UI."),
    Flashcard(6, 97, "What is 'remember' in Compose?", "A function used to store an object in memory during recomposition."),
    Flashcard(6, 98, "What is a 'Side-effect' in Compose?", "A change to the state of the app that happens outside the scope of a Composable function."),
    Flashcard(6, 99, "What is a Modifier in Compose?", "An ordered collection of elements that decorate or augment a Composable."),
    Flashcard(6, 100, "What is a 'Scaffold' in Compose?", "A Composable that provides a standard layout structure for common UI components."),
    Flashcard(6, 101, "What is 'CompositionLocal'?", "A way to pass data through the Composition tree without explicitly passing it as a parameter."),
    Flashcard(6, 102, "What is 'derivedStateOf'?", "A function used to create a State that depends on other State objects, optimizing recomposition."),
    Flashcard(6, 103, "What is a 'LazyColumn'?", "A vertically scrolling list that only composes and lays out currently visible items."),
    Flashcard(6, 104, "What is 'LaunchedEffect'?", "A Composable used to run side effects in the scope of a Composable, cancelled when it leaves composition."),
    Flashcard(6, 105, "What is 'rememberUpdatedState'?", "A way to reference a value in an effect that shouldn't restart when the value changes."),
    Flashcard(6, 106, "What is 'rememberSaveable'?", "A function similar to 'remember', but it survives Activity or process recreation by saving state in a Bundle."),
    Flashcard(6, 107, "What is a 'Slot API'?", "A pattern where a Composable accepts other Composables as parameters, allowing for highly customizable components."),
    Flashcard(6, 108, "What is 'Unidirectional Data Flow' (UDF)?", "A design pattern where state flows down to UI components and events flow up to the state holder."),
    Flashcard(6, 109, "What is 'animate*AsState'?", "A family of functions used to create simple animations that transition between values when the target state changes."),
    Flashcard(6, 110, "What is 'DisposableEffect'?", "A side-effect API used for effects that need to be cleaned up when the Composable leaves the composition."),
    Flashcard(6, 111, "What is 'SideEffect'?", "An API used to share Compose state with non-Compose objects, running after every successful recomposition."),
    Flashcard(6, 112, "What is 'produceState'?", "A side-effect API that converts non-Compose state (like Flow or LiveData) into a Compose State object."),
    Flashcard(6, 113, "What is 'Intrinsic Measurement'?", "A feature that allows Compose to query children about their preferred size before the actual measurement pass."),
    Flashcard(6, 114, "What is 'Semantics' in Compose?", "A mechanism used to describe the UI for accessibility services and the testing framework."),
    Flashcard(6, 115, "What is 'SubcomposeLayout'?", "A layout that allows for deferred composition, where the composition of some children depends on the measurement of others."),
    Flashcard(6, 116, "What is 'BoxWithConstraints'?", "A layout Composable that provides its measurement constraints to its content, useful for responsive designs."),
    Flashcard(6, 117, "What is 'LocalConfiguration'?", "A CompositionLocal that provides access to the current device Configuration (e.g., orientation, dimensions)."),
    Flashcard(6, 118, "What is 'PointerInput'?", "A modifier used to handle low-level touch, mouse, or stylus input events."),
    Flashcard(6, 119, "What is 'FocusRequester'?", "An object used to programmatically request or change focus for a specific UI element."),
    Flashcard(6, 120, "What is 'WindowInsets'?", "An API used to handle system UI elements like the status bar, ensuring content doesn't overlap with them."),
    Flashcard(6, 121, "What is 'VisualTransformation'?", "An interface used to change the visual representation of input text, such as for password masking."),
    Flashcard(6, 122, "What is 'Brush' in Compose?", "An object used to define how to fill an area with color, gradients, or patterns when drawing shapes or text.")
)
