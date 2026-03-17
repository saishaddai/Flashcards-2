package com.saishaddai.flashcards.model.fcdata

import com.saishaddai.flashcards.model.Flashcard

val androidCards = listOf(
    Flashcard(
        2,
        23,
        "What are the core building blocks of an Android Application?",
        "Activities, Fragments, Services, Content Providers, Broadcast Receivers, and Views."
    ),
    Flashcard(
        2,
        24,
        "What is an Activity?",
        "A component that provides a screen with which users can interact."
    ),
    Flashcard(2, 25, "What is a Fragment?", "A reusable portion of an Activity's user interface."),
    Flashcard(
        2,
        26,
        "What is a Service?",
        "A component that performs long-running operations in the background."
    ),
    Flashcard(
        2,
        27,
        "What is a Content Provider?",
        "A component that manages access to a structured set of data."
    ),
    Flashcard(
        2,
        28,
        "What is a Broadcast Receiver?",
        "A component that responds to system-wide broadcast announcements."
    ),
    Flashcard(2, 29, "What is a View?", "A basic building block for user interface components."),
    Flashcard(
        2,
        30,
        "What are Layouts?",
        "Containers that define the structure for user interface components."
    ),
    Flashcard(
        2,
        31,
        "What is the Manifest File?",
        "An XML file that defines essential information about an app to the Android system."
    ),
    Flashcard(
        2,
        32,
        "What is an Intent?",
        "A messaging object used to request an action from another app component."
    ),
    Flashcard(
        2,
        33,
        "What is an Explicit Intent?",
        "An Intent that specifies the exact component to start by name."
    ),
    Flashcard(
        2,
        34,
        "What is an Implicit Intent?",
        "An Intent that declares a general action to perform, letting the system find a match."
    ),
    Flashcard(
        2,
        35,
        "What is the Android Lifecycle?",
        "A set of states an app component goes through from creation to destruction."
    ),
    Flashcard(
        2,
        36,
        "What is an Activity Lifecycle?",
        "The specific sequence of states an Activity goes through, like onCreate and onResume."
    ),
    Flashcard(
        2,
        37,
        "What is an Application Context in Android?",
        "A Context tied to the lifecycle of the entire application process."
    ),
    Flashcard(
        2,
        38,
        "What is Activity Context in Android?",
        "A Context tied to the lifecycle of a specific Activity."
    ),
    Flashcard(
        2,
        39,
        "What is the Fragment Lifecycle?",
        "The sequence of states a Fragment goes through, closely tied to its host Activity."
    ),
    Flashcard(
        2,
        40,
        "What is the difference between replacing and adding fragments in the backstack?",
        "Replace removes the existing fragment while add overlays the new one."
    ),
    Flashcard(
        2,
        41,
        "What happens when the device is moved from Portrait mode to Landscape?",
        "By default, the current Activity is destroyed and recreated."
    ),
    Flashcard(
        2,
        42,
        "What does RTL mean in UI Design?",
        "Right-to-Left, a design approach for languages like Arabic or Hebrew."
    ),
    Flashcard(
        2,
        43,
        "What is an Intent Service?",
        "A base class for Services that handle asynchronous requests on a separate thread."
    ),
    Flashcard(
        2,
        44,
        "What is an Async Task?",
        "A deprecated class for performing background operations and publishing results on the UI thread."
    ),
    Flashcard(2, 45, "What is a Thread?", "An independent path of execution within a program."),
    Flashcard(
        2,
        46,
        "What is Parcelable in Android?",
        "An interface for classes whose instances can be written to and restored from a Parcel."
    ),
    Flashcard(
        2,
        47,
        "Serializable vs Parcelable?",
        "Serializable is a standard Java interface, while Parcelable is Android-specific and faster."
    ),
    Flashcard(
        2,
        48,
        "What is a Handler?",
        "A class used to send and process Message and Runnable objects associated with a thread's MessageQueue."
    ),
    Flashcard(
        2,
        49,
        "What is a StickyIntent?",
        "A deprecated type of broadcast that stays around after it's been sent."
    ),
    Flashcard(
        2,
        50,
        "What is a PendingIntent?",
        "A description of an Intent and target action to be performed later by another app."
    ),
    Flashcard(
        2,
        51,
        "What are IntentFilters?",
        "Expressions in the manifest that specify the types of Intents a component can receive."
    ),
    Flashcard(
        2,
        52,
        "What are Loaders in Android?",
        "Deprecated components that make it easy to asynchronously load data in an Activity or Fragment."
    ),
    Flashcard(
        2,
        53,
        "What is ConstraintLayout?",
        "A flexible layout manager that allows for complex UI structures with flat view hierarchies."
    ),
    Flashcard(
        2,
        54,
        "What is ANR?",
        "Application Not Responding, an error when the UI thread is blocked for too long."
    ),
    Flashcard(
        2,
        55,
        "What is commit() in SharedPreferences?",
        "A synchronous method to save changes to SharedPreferences."
    ),
    Flashcard(
        2,
        56,
        "What is apply() in SharedPreferences?",
        "An asynchronous method to save changes to SharedPreferences."
    ),
    Flashcard(
        2,
        57,
        "How to support different screen sizes?",
        "By using flexible layouts, resource qualifiers, and vector drawables."
    ),
    Flashcard(
        2,
        58,
        "What is RecyclerView?",
        "A flexible and efficient version of ListView for displaying large data sets."
    ),
    Flashcard(
        2,
        59,
        "What is the ‘ViewHolderPattern’ in RecyclerView?",
        "A pattern that improves performance by caching view references to avoid repeated findViewById calls."
    ),
    Flashcard(
        2,
        60,
        "What is AAPT?",
        "Android Asset Packaging Tool, which compiles resources into binary format."
    ),
    Flashcard(
        2,
        61,
        "What is AIDL?",
        "Android Interface Definition Language, used to define the interface for inter-process communication."
    ),
    Flashcard(
        2,
        62,
        "What is ‘JobScheduler’?",
        "An API for scheduling background tasks to be executed under specific conditions."
    ),
    Flashcard(
        2,
        63,
        "What are Storage methods?",
        "Options include SharedPreferences, internal storage, external storage, and SQLite."
    ),
    Flashcard(
        2,
        64,
        "What is ‘WorkManager’?",
        "The recommended API for persistent, deferrable background work in Android."
    ),
    Flashcard(
        2,
        65,
        "What is the Zygote process?",
        "A parent process in Android that pre-loads common classes and resources to speed up app launching."
    ),
    Flashcard(
        2,
        66,
        "How do Fragments communicate?",
        "Primarily through a shared ViewModel or via the host Activity."
    ),
    Flashcard(
        2,
        67,
        "What is a Binder?",
        "A mechanism for inter-process communication in Android."
    ),
    Flashcard(
        2,
        68,
        "What is ‘ANR Watchdog’?",
        "A tool or pattern used to detect and log Application Not Responding situations."
    ),
    Flashcard(2, 69, "What is a Looper?", "A class that runs a message loop for a thread."),
    Flashcard(
        2,
        70,
        "What is a MessageQueue?",
        "A queue that holds the list of messages to be dispatched by a Looper."
    ),
    Flashcard(
        2,
        71,
        "What the Main thread in Android?",
        "The primary thread responsible for handling UI events and updates."
    ),
    Flashcard(
        2,
        72,
        "ApplicationContext vs BaseContext?",
        "ApplicationContext is global, while BaseContext is specific to the component it's in."
    ),
    Flashcard(
        2,
        73,
        "What is a Task in backstack?",
        "A collection of activities that users interact with when performing a certain job."
    ),
    Flashcard(
        2,
        74,
        "What is ‘PackageManager’?",
        "A class for retrieving various kinds of information related to application packages."
    ),
    Flashcard(
        2,
        75,
        "What is DVM?",
        "Dalvik Virtual Machine, the original runtime used by Android to execute app code."
    ),
    Flashcard(
        2,
        76,
        "What is ART?",
        "Android Runtime, the modern runtime that replaced DVM with ahead-of-time compilation."
    ),
    Flashcard(
        2,
        77,
        "What is the purpose of the file proguard-rules.pro?",
        "To define rules for shrinking, optimizing, and obfuscating app code."
    ),
    Flashcard(
        2,
        78,
        "What are the ‘.dex’ files?",
        "Compiled Android application code that runs on the Dalvik or ART runtimes."
    ),
    Flashcard(
        2,
        79,
        "What is the Android boot process?",
        "The sequence from power-on to the system being ready, including Bootloader, Kernel, and Zygote."
    ),
    Flashcard(
        2,
        80,
        "What is ‘compile’ in Gradle?",
        "A deprecated configuration for adding dependencies; use implementation or api instead."
    ),
    Flashcard(
        2,
        81,
        "What is ‘implementation’ in Gradle?",
        "A configuration that keeps dependencies private to the module, improving build times."
    ),
    Flashcard(
        2,
        82,
        "What is ‘api’ in Gradle?",
        "A configuration that exposes dependencies to other modules that depend on it."
    ),
    Flashcard(
        2,
        83,
        "What is 'libs.versions.toml‘ in Gradle?",
        "A version catalog file used to manage dependencies and versions centrally."
    ),
    Flashcard(
        2,
        84,
        "What is the difference between build.gradle (Project and Module)?",
        "Project-level defines global settings, while module-level defines specific settings for that module."
    ),
    Flashcard(
        2,
        85,
        "What is versionName and versionCode in Gradle?",
        "versionCode is an internal integer, while versionName is a string shown to users."
    ),
    Flashcard(
        2,
        86,
        "What are flavors in Gradle?",
        "Configurations used to create different versions of an app from the same codebase."
    ),
    Flashcard(
        2,
        87,
        "Flavors vs buildTypes?",
        "Flavors define what is built (e.g., free vs pro), while buildTypes define how it is built (e.g., debug vs release)."
    ),
    Flashcard(
        2,
        88,
        "What is a Work Request?",
        "An object that defines how and when a task should be run by WorkManager."
    ),
    Flashcard(
        2,
        89,
        "What are the result types in WorkManager?",
        "The possible outcomes: Success, Failure, and Retry."
    ),
    Flashcard(
        2,
        90,
        "Which are the states of a Work in WorkManager?",
        "Enqueued, running, succeeded, failed, blocked, and cancelled."
    ),
    Flashcard(
        2,
        91,
        "Worker vs CoroutineWorker?",
        "Worker uses a background thread, while CoroutineWorker is optimized for Kotlin coroutines."
    ),
    Flashcard(
        2,
        92,
        "WorkManager vs JobScheduler?",
        "WorkManager is a higher-level API that uses JobScheduler and other mechanisms under the hood."
    ),
)