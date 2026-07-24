# Testing Navigation with Navigation 3

The "proper" way to test navigation in an Android app involves verifying two things:
1.  **State Integrity**: Ensuring that user interactions (like clicking a button) update the navigation state (the BackStack) correctly.
2.  **UI Representation**: Ensuring that the UI correctly reflects the current state by displaying the expected screen.

Since this project uses **Navigation 3**, which manages navigation via a simple list-based `NavBackStack`, we can test it efficiently by hoisting the navigation state.

## Proposed Changes

### [Navigation Component]

#### [MODIFY] [NavigationWrapper.kt](file:///Users/sai/Projects/Flashcards-2/app/src/main/java/com/saishaddai/flashcards/navigation/NavigationWrapper.kt)
- Refactor `NavigationWrapper` to accept `backStack: NavBackStack<NavKey>` as a parameter.
- Use `rememberNavBackStack(Route.DeckList)` as the default value.
- This allows tests to provide a pre-configured backstack and inspect its state after UI actions.

#### [NEW] [NavigationTest.kt](file:///Users/sai/Projects/Flashcards-2/app/src/androidTest/java/com/saishaddai/flashcards/navigation/NavigationTest.kt)
- Implement instrumentation tests to verify core navigation flows:
    - **Bottom Navigation**: Verify clicking on "Stats", "Instructions", and "Settings" updates the backstack and shows the respective screens.
    - **Session Start**: Verify that selecting a deck and clicking "Start Session" navigates to the `FlashcardSession` route.
    - **Back Navigation**: Verify that the back button (or system back) correctly pops the backstack.

## Verification Plan

### Automated Tests
- Run the new `NavigationTest` using:
  ```bash
  ./gradlew connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.saishaddai.flashcards.navigation.NavigationTest
  ```

### Manual Verification
- Run the app on a device/emulator to ensure that the refactoring didn't break the user experience.

> [!NOTE]
> Navigation 3 is state-driven. By hoisting the `NavBackStack`, we make navigation logic as testable as any other UI state in Compose.
