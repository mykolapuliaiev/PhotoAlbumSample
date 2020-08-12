package com.example.photoalbumsample

import android.app.Application

/**
 * Application class.
 */
class SampleApplication: Application() {

    companion object {
        lateinit var appContext: SampleApplication
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}