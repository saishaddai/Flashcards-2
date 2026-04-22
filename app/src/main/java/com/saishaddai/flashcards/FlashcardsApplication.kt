package com.saishaddai.flashcards

import android.app.Application
import com.saishaddai.flashcards.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FlashcardsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@FlashcardsApplication)
            modules(appModule)
        }
    }
}
