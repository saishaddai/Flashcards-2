package com.saishaddai.flashcards.model.fcdata

import com.saishaddai.flashcards.model.Flashcard

val navigationCards = listOf(
    Flashcard(10, 1001, "What is the NavHost in Jetpack Navigation?", "A container that displays the current destination from the navigation graph."),
    Flashcard(10, 1002, "What is a NavGraph?", "An XML or Kotlin DSL file that contains all navigation-related information, including destinations and actions."),
    Flashcard(10, 1003, "What does NavController do?", "It is the object that manages app navigation within a NavHost, handling transitions between destinations."),
    Flashcard(10, 1004, "How do you navigate to a destination by route in Compose?", "By calling navController.navigate(\"route_name\")."),
    Flashcard(10, 1005, "What is a 'Route' in Jetpack Navigation Compose?", "A unique string (or serialized object in Nav3) that identifies a destination."),
    Flashcard(10, 1006, "What is popBackStack() used for?", "To remove the current destination from the back stack and return to the previous one."),
    Flashcard(10, 1007, "What is the purpose of 'launchSingleTop'?", "It prevents multiple instances of the same destination from being added to the top of the back stack."),
    Flashcard(10, 1008, "What is 'popUpTo' used for when navigating?", "To clear the back stack up to a specific destination before performing the navigation."),
    Flashcard(10, 1009, "What is the 'inclusive' parameter in popUpTo?", "If true, it also pops the destination specified in popUpTo from the back stack."),
    Flashcard(10, 1010, "How do you pass arguments in Navigation Compose (pre-Type Safety)?", "By appending them to the route string, like 'details/{id}' and defining them in the composable function."),
    Flashcard(10, 1011, "What is Safe Args?", "A Gradle plugin that generates type-safe directions and argument classes for navigation (primarily for XML-based navigation)."),
    Flashcard(10, 1012, "What is 'Deep Linking' in Navigation?", "The ability to navigate directly to a specific destination in your app from an external source (like a URL or Notification)."),
    Flashcard(10, 1013, "What is Navigation 3 (Nav3)?", "The next evolution of Jetpack Navigation that focuses on type-safe, multi-platform, and simplified navigation APIs."),
    Flashcard(10, 1014, "How does Navigation 3 handle type safety differently?", "It uses Kotlin Serialization and objects/classes as routes instead of manually parsed strings."),
    Flashcard(10, 1015, "What is a NavBackStackEntry?", "A class that represents a destination in the back stack and provides access to its arguments, lifecycle, and ViewModelStore."),
    Flashcard(10, 1016, "What is the benefit of using Hilt with Navigation?", "It allows you to get a ViewModel scoped to a specific navigation graph using hiltNavGraphViewModel()."),
    Flashcard(10, 1017, "What is the 'startDestination'?", "The destination that is automatically loaded when the NavHost is first displayed."),
    Flashcard(10, 1018, "How do you define an argument as optional in a route?", "By using query parameter syntax: 'route?argName={argName}'."),
    Flashcard(10, 1019, "What is a 'Nested Navigation Graph'?", "A way to group related destinations into a modular sub-graph, useful for encapsulating complex flows like login or checkout."),
    Flashcard(10, 1020, "What is 'NavOptions'?", "A class used to define specific navigation behaviors like custom animations, singleTop, and popUpTo."),
    Flashcard(10, 1021, "How to handle bottom navigation with Jetpack Navigation?", "By linking the BottomNavigationView or Compose NavigationBar items to the NavController via routes."),
    Flashcard(10, 1022, "What is the purpose of the 'NavHostController'?", "A subclass of NavController that provides additional methods for setup with a NavHost (e.g., setLifecycleOwner)."),
    Flashcard(10, 1023, "Best Practice: Navigation Logic location?", "Keep navigation logic outside of individual composables; pass lambdas to composables to handle navigation events."),
    Flashcard(10, 1024, "What is 'restoreState' in navigation options?", "It determines whether to restore the saved state of a destination when it was previously popped via popUpTo."),
    Flashcard(10, 1025, "What is an 'Action' in XML navigation?", "A definition of a transition between two destinations, often including animations and argument passing."),
    Flashcard(10, 1026, "How to pass complex objects in Navigation (pre-Nav3)?", "The recommended way was to pass an ID and fetch the object from a repository, or use Parcelable if necessary (though discouraged for large data)."),
    Flashcard(10, 1027, "What is the 'currentBackStackEntryAsState()' function?", "A Compose utility to observe the current navigation destination as a State object."),
    Flashcard(10, 1028, "What is the 'OnBackPressedCallback'?", "An API used to intercept the system back button press to perform custom logic before or instead of popping the back stack."),
    Flashcard(10, 1029, "How to provide custom transitions in Navigation Compose?", "By using the 'enterTransition', 'exitTransition', etc., parameters in the 'composable()' or 'navigation()' functions."),
    Flashcard(10, 1030, "What is the 'NavType' class used for?", "It defines how an argument is parsed from a route string (e.g., IntType, StringType, BoolType).")
)
