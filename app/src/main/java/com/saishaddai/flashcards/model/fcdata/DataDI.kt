package com.saishaddai.flashcards.model.fcdata

import com.saishaddai.flashcards.model.Flashcard

val diCards = listOf(
    // General DI
    Flashcard(8,  101, "What is Dependency Injection?", "A design pattern where an object receives its dependencies from an external source rather than creating them itself."),
    Flashcard(8,  102, "What is the main benefit of DI?", "It promotes loose coupling, making code more modular, testable, and maintainable."),
    Flashcard(8,  103, "What is the difference between DI and Service Locator?", "DI provides dependencies to the class (push), while Service Locator requires the class to request them (pull)."),
    Flashcard(8,  104, "What are the three common types of DI?", "Constructor Injection, Setter Injection, and Interface/Field Injection."),
    Flashcard(8,  105, "Why is Constructor Injection preferred?", "It ensures the object is always in a valid state and makes dependencies explicit and immutable."),

    // Dagger & Hilt
    Flashcard(8,  106, "What is Dagger 2?", "A fully static, compile-time dependency injection framework for Java, Kotlin, and Android."),
    Flashcard(8,  107, "What does @Inject do in Dagger?", "It marks a constructor, field, or method that Dagger should use to provide or satisfy a dependency."),
    Flashcard(8,  108, "What is a @Module in Dagger?", "A class annotated with @Module that contains methods (annotated with @Provides or @Binds) to instruct Dagger how to create dependencies."),
    Flashcard(8,  109, "What is a @Component in Dagger?", "An interface that acts as a bridge between modules and the classes that require dependencies."),
    Flashcard(8,  110, "What is Hilt?", "A dependency injection library for Android that is built on top of Dagger to simplify its usage and boilerplate."),
    Flashcard(8,  111, "What does @HiltAndroidApp do?", "It triggers Hilt's code generation and must be applied to the Application class."),
    Flashcard(8,  112, "What is @AndroidEntryPoint?", "An annotation used on Android components (Activities, Fragments, Views) to enable field injection."),
    Flashcard(8,  113, "What is the purpose of @InstallIn in Hilt?", "It specifies which Hilt component (e.g., SingletonComponent, ActivityComponent) a module should be installed into."),
    Flashcard(8,  114, "What is @ViewModelInject (deprecated) replaced by in Hilt?", "@HiltViewModel."),
    Flashcard(8,  115, "What is a @Qualifier in Dagger/Hilt?", "An annotation used to distinguish between multiple bindings of the same type."),

    // Koin
    Flashcard(8,  116, "What is Koin?", "A pragmatic lightweight dependency injection framework for Kotlin (including KMP) that uses a DSL instead of code generation."),
    Flashcard(8,  117, "How does Koin differ from Dagger/Hilt?", "Koin resolves dependencies at runtime, while Dagger/Hilt does so at compile-time."),
    Flashcard(8,  118, "What does the 'single' keyword mean in Koin?", "It defines a singleton definition, which provides the same instance throughout the app's lifecycle."),
    Flashcard(8,  119, "What does 'factory' do in Koin?", "It provides a new instance of the requested type every time it is injected."),
    Flashcard(8,  120, "How do you start Koin in an Android app?", "By calling the startKoin { } function, usually in the Application class."),
    Flashcard(8,  121, "What is 'get()' in a Koin module?", "A function used to automatically resolve and inject a constructor dependency."),
    Flashcard(8,  122, "What is 'by inject()' in Koin?", "A Kotlin property delegate used to lazily inject dependencies into classes."),
    Flashcard(8,  123, "How do you inject a ViewModel in Koin?", "By using the 'viewModel' keyword in the module and 'by viewModel()' in the Fragment/Activity."),
    Flashcard(8,  124, "What is 'koin-annotations'?", "An optional library that adds compile-time safety and annotation support to Koin."),

    // KMP & Advanced
    Flashcard(8,  125, "Which DI framework is most popular for Kotlin Multiplatform (KMP)?", "Koin is widely used due to its pure Kotlin implementation and lack of code generation requirements."),
    Flashcard(8,  126, "Can you use Hilt in a KMP 'commonMain' source set?", "No, Hilt is Android-specific. You would need to use Koin or manual DI."),
    Flashcard(8,  127, "What is 'Scoping' in DI?", "Limiting the lifetime of a dependency to match a specific component's lifecycle (e.g., Activity or Fragment)."),
    Flashcard(8,  128, "What is the 'Dependency Inversion Principle'?", "High-level modules should not depend on low-level modules; both should depend on abstractions."),
    Flashcard(8,  129, "What is a 'Circular Dependency'?", "A situation where two or more classes depend on each other, preventing DI frameworks from initializing them."),
    Flashcard(8,  130, "What is 'Inversion of Control' (IoC)?", "A principle where the control flow of a program is inverted: instead of the programmer calling a library, the framework calls the programmer's code.")
)
