package com.example.plainolnotes4

import android.app.Application
import timber.log.Timber

class PlainOlNotes4App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}