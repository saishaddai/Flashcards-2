package com.saishaddai.flashcards.model

enum class DeckType(val id: Int, val jsonFile: String = "") {
    OOP(1, "oop.json"),
    ANDROID_CORE(2, "android.json"),
    KOTLIN(3, "kotlin.json"),
    KOTLIN_MP(4, "kmp.json"),
    SECURITY(5, "security.json"),
    COMPOSE(6, "compose.json"),
    DATABASES(7, "databases.json"),
    DAGGER_HILT(8, "dagger.json"),
    MATERIAL_3(9, "material.json"),
    NAVIGATION(10, "navigation.json"),
    JETPACK(11, "jetpack.json"),
    TESTING(12, "testing.json"),
    GRADLE(13, "gradle.json"),
    ANDROID_OPS(14, "androidOps.json"),
    LIBRARIES(15, "libraries.json"),
    DESIGN_PATTERNS(16, "patterns.json"),
    COROUTINES(17, "coroutines.json"),
    FIREBASE(18, "firebase.json"),
    GRAPHQL(19, "graphql.json")
}