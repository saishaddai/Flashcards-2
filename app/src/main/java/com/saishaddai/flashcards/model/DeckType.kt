package com.saishaddai.flashcards.model

enum class DeckType(val id: Int, val friendlyName: String = "", val jsonFile: String = "") {
    OOP(id = 1, friendlyName = "OOP", jsonFile = "oop.json"),
    ANDROID_CORE(id = 2, friendlyName = "Android", jsonFile = "android.json"),
    KOTLIN(id = 3, friendlyName = "Kotlin", jsonFile = "kotlin.json"),
    KOTLIN_MP(id = 4, friendlyName = "Kotlin Multi Platform", jsonFile = "kmp.json"),
    SECURITY(id = 5, friendlyName = "Security", jsonFile = "security.json"),
    COMPOSE(id = 6, friendlyName = "Jetpack Compose", jsonFile = "compose.json"),
    DATABASES(id = 7, friendlyName = "Databases", jsonFile = "databases.json"),
    DI(id = 8, friendlyName = "Dependency Injection", jsonFile = "di.json"),
    MATERIAL_3(id = 9, friendlyName = "Material Design", jsonFile = "material.json"),
    NAVIGATION(id = 10, friendlyName = "Navigation", jsonFile = "navigation.json"),
    JETPACK(id = 11, friendlyName = "Jetpack", jsonFile = "jetpack.json"),
    TESTING(id = 12, friendlyName = "Testing", jsonFile = "testing.json"),
    GRADLE(id = 13, friendlyName = "Gradle", jsonFile = "gradle.json"),
    ANDROID_OPS(id = 14, friendlyName = "CD/CI", jsonFile = "androidOps.json"),
    LIBRARIES(id = 15, friendlyName = "Libraries", jsonFile = "libraries.json"),
    DESIGN_PATTERNS(id = 16, friendlyName = "Design Patterns", jsonFile = "patterns.json"),
    COROUTINES(id = 17, friendlyName = "Coroutines", jsonFile = "coroutines.json"),
    FIREBASE(id = 18, friendlyName = "Firebase", jsonFile = "firebase.json"),
    GRAPHQL(id = 19, friendlyName = "GraphQL", jsonFile = "graphql.json"),
    SENSORS(id = 20, friendlyName = "Sensors", jsonFile = "sensors.json");

    companion object {
        fun fromId(id: Int): DeckType = entries.find { it.id == id } ?: OOP
    }
}