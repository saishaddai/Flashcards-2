# Implementation Plan - Repository Refactoring and Test Coverage

This plan outlines the refactoring of the `com.saishaddai.flashcards.repository.impl` package to unify the naming convention, improve code organization, and achieve comprehensive unit test coverage.

## User Review Required

> [!IMPORTANT]
> The renaming of repositories will affect the Dependency Injection setup (likely Hilt or manual DI). I will need to update the DI modules/providers as well.

## Proposed Changes

### Repository Renaming
Unify the naming convention by using the `Local...Repository` prefix for all local persistence implementations.

#### [MODIFY] `OfflineDeckRepository` -> `LocalDeckRepository`
#### [MODIFY] `RoomFlashcardRepository` -> `LocalFlashcardRepository`
#### [MODIFY] `RoomStatsRepository` -> `LocalStatsRepository`
#### [MODIFY] `RoomStudyRepository` -> `LocalStudyRepository`
#### [MODIFY] `RoomSessionRepository` -> `LocalSessionRepository`
#### [MODIFY] `DataStoreSettingsRepository` -> `LocalSettingsRepository`

### New Classes (Data Sources)
Extract data-specific logic into separate data sources to improve testability and separate concerns.

#### [NEW] `FlashcardAssetDataSource`
- Extract the JSON asset loading logic from `LocalFlashcardRepository`.
- This allows testing `LocalFlashcardRepository` without mocking the `Context` and `AssetManager` for JSON parsing.

### Unit Test Coverage
Create or update unit tests for all repositories in the `impl` package.

#### [NEW] `LocalStatsRepositoryTest`
- Test weekly activity mapping.
- Test skill mastery calculation.
- Test streak and study time logic.

#### [NEW] `LocalStudyRepositoryTest`
- Test session completion logic.
- Test progress calculation integration with `SessionCalculator`.
- Test daily goal verification.

#### [NEW] `LocalSessionRepositoryTest`
- Test standard CRUD operations for sessions.

#### [MODIFY] Update existing tests
- Rename and update `OfflineDeckRepositoryTest` to `LocalDeckRepositoryTest`.
- Rename and update `RoomFlashcardRepositoryTest` to `LocalFlashcardRepositoryTest`.
- Rename and update `DataStoreSettingsRepositoryTest` to `LocalSettingsRepositoryTest`.

## Verification Plan

### Automated Tests
- Run all unit tests in the `com.saishaddai.flashcards.repository.impl` package:
  `./gradlew testDebugUnitTest --tests "com.saishaddai.flashcards.repository.impl.*"`
- Verify that the app still compiles and runs after renaming (DI verification).
