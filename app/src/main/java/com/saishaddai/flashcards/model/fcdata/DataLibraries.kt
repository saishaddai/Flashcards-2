package com.saishaddai.flashcards.model.fcdata

import com.saishaddai.flashcards.model.Flashcard

val librariesCards = listOf(
    Flashcard(15, 1501, "What is Retrofit?", "A type-safe HTTP client for Android and Java by Square that turns your HTTP API into a Java/Kotlin interface."),
    Flashcard(15, 1502, "What is the role of OkHttp in relation to Retrofit?", "Retrofit is built on top of OkHttp; it handles the high-level API mapping while OkHttp handles the actual network connections and low-level HTTP details."),
    Flashcard(15, 1503, "What is an Interceptor in OkHttp?", "A powerful mechanism that can observe, modify, and potentially short-circuit requests and responses (e.g., for adding Auth headers or logging)."),
    Flashcard(15, 1504, "Moshi vs. Gson: Which is generally preferred for modern Kotlin apps?", "Moshi is generally preferred because it has better Kotlin support (handling nullability and default values) and is more lightweight."),
    Flashcard(15, 1505, "What is the purpose of @SerializedName in Gson?", "It is used to map a JSON key name to a different field name in your Java/Kotlin class."),
    Flashcard(15, 1506, "What is the purpose of @Json(name = \"...\") in Moshi?", "It provides the same functionality as Gson's @SerializedName, mapping JSON keys to class properties."),
    Flashcard(15, 1507, "What is Glide primarily used for?", "Efficient image loading and caching, optimized for smooth scrolling in lists by managing memory and bitmap re-use."),
    Flashcard(15, 1508, "What is Coil?", "An image loading library for Android backed by Kotlin Coroutines, which is lightweight and integrates natively with Jetpack Compose."),
    Flashcard(15, 1509, "Why might you choose Coil over Glide in a new Compose project?", "Coil is built specifically for Kotlin and Compose, uses Coroutines for modern concurrency, and has a smaller method count."),
    Flashcard(15, 1510, "What is Koin?", "A pragmatic lightweight dependency injection (or service locator) framework for Kotlin developers, using a DSL instead of code generation."),
    Flashcard(15, 1511, "Hilt vs. Koin: What is a major difference in how they work?", "Hilt uses compile-time code generation (via annotation processing) to ensure safety, while Koin resolves dependencies at runtime."),
    Flashcard(15, 1512, "What is Timber?", "A small, extensible logging library built on top of Android's Log class that helps manage logging in different build types (e.g., stripping logs in release)."),
    Flashcard(15, 1513, "What is LeakCanary?", "A memory leak detection library for Android that automatically detects and notifies you about memory leaks during development."),
    Flashcard(15, 1514, "What is Lottie?", "A library by Airbnb that parses Adobe After Effects animations exported as JSON with Bodymovin and renders them natively on mobile."),
    Flashcard(15, 1515, "What is MockK?", "A mocking library specifically designed for Kotlin, supporting features like final classes, coroutines, and extension functions better than Mockito."),
    Flashcard(15, 1516, "What is Robolectric?", "A testing framework that allows you to run Android tests on a local JVM instead of a device or emulator, making them much faster."),
    Flashcard(15, 1517, "What is Turbine used for?", "A small library for testing Kotlin Flows, providing a simple way to await and assert emissions."),
    Flashcard(15, 1518, "What is Chucker?", "An in-app HTTP inspector that allows you to view all network requests and responses directly on your device via a notification."),
    Flashcard(15, 1519, "What is the benefit of using 'Kotlin Serialization'?", "It is a multiplatform-ready library that doesn't rely on reflection (when using the compiler plugin), making it fast and compatible with KMP."),
    Flashcard(15, 1520, "What is Apollo Kotlin?", "A strongly-typed, caching GraphQL client for the JVM and Android, generating models from your GraphQL queries."),
    Flashcard(15, 1521, "Best Practice: Should you use a 3rd party library directly in your UI code?", "No; it's often better to wrap the library or hide it behind an interface (e.g., an ImageLoader interface) to make switching libraries easier later."),
    Flashcard(15, 1522, "What is 'Okio'?", "A library by Square that complements java.io and java.nio to make it much easier to access, store, and process your data (used internally by OkHttp)."),
    Flashcard(15, 1523, "What is a 'Converter.Factory' in Retrofit?", "A component that tells Retrofit how to serialize and deserialize data (e.g., converting JSON into Kotlin objects using Moshi or Gson)."),
    Flashcard(15, 1524, "What is an 'Authenticator' in OkHttp?", "A specific interceptor-like component designed to handle 401 Unauthorized responses and perform token refreshes automatically."),
    Flashcard(15, 1525, "What is 'SQLDelight'?", "A library that generates typesafe Kotlin APIs from your SQL statements, supporting both Android and Multiplatform."),
    Flashcard(15, 1526, "How does Glide handle 'CenterCrop'?", "It scales the image such that it fills the dimensions of the ImageView and crops the additional parts, avoiding distortion."),
    Flashcard(15, 1527, "What is the 'every' block in MockK?", "A DSL construct used to define the behavior of a mocked function (similar to 'when' in Mockito)."),
    Flashcard(15, 1528, "What is 'Coil's ImageLoader'?", "The central class that handles requests, caching, and image decoding; it can be customized or provided via DI."),
    Flashcard(15, 1529, "Why should you avoid 'Auto-mocking' everything in tests?", "It can lead to brittle tests that are tied to implementation details rather than behavior; use real objects for state-only classes (POJOs/Data classes)."),
    Flashcard(15, 1530, "What is 'Stetho' (now mostly replaced by Flipper or Layout Inspector)?", "A debug bridge for Android that allowed developers to use Chrome DevTools to inspect network, database, and UI (legacy).")
)
