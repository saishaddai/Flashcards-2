package com.saishaddai.flashcards.di

import com.saishaddai.flashcards.model.Deck
import com.saishaddai.flashcards.model.DeckType
import com.saishaddai.flashcards.model.Flashcard
import com.saishaddai.flashcards.repository.DeckRepository
import com.saishaddai.flashcards.repository.FlashcardRepository
import com.saishaddai.flashcards.repository.HardcodedStatsRepository
import com.saishaddai.flashcards.repository.SettingsRepository
import com.saishaddai.flashcards.repository.StatsRepository
import com.saishaddai.flashcards.repository.SessionRepository
import com.saishaddai.flashcards.repository.impl.DataStoreSettingsRepository
import com.saishaddai.flashcards.repository.impl.HardcodedSessionRepository
import com.saishaddai.flashcards.repository.impl.JSONDeckRepository
import com.saishaddai.flashcards.repository.impl.JSONFlashcardRepository
import com.saishaddai.flashcards.viewmodel.DecksViewModel
import com.saishaddai.flashcards.viewmodel.FinishSessionViewModel
import com.saishaddai.flashcards.viewmodel.FlashcardViewModel
import com.saishaddai.flashcards.viewmodel.SettingsViewModel
import com.saishaddai.flashcards.viewmodel.StatsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single<FlashcardRepository<DeckType, Flashcard>> { JSONFlashcardRepository(androidContext()) }
    single<SessionRepository> { HardcodedSessionRepository() }
    single<DeckRepository<Deck>> { JSONDeckRepository(get(), get()) }
    single<StatsRepository> { HardcodedStatsRepository() }
    single<SettingsRepository> { DataStoreSettingsRepository(androidContext()) }

    // ViewModels
    viewModel { DecksViewModel(androidApplication(), get()) }
    viewModel { (deckId: Int) -> FlashcardViewModel(androidApplication(), deckId, get(), get()) }
    viewModel { StatsViewModel(get()) }
    viewModel { FinishSessionViewModel() }
    viewModel { SettingsViewModel(get()) }
}
