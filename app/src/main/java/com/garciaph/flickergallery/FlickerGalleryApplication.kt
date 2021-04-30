package com.garciaph.flickergallery

import android.app.Application
import timber.log.Timber

class FlickerGalleryApplication: Application() {

    companion object {
        lateinit var INSTANCE: FlickerGalleryApplication
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        Timber.plant(Timber.DebugTree())
    }
}