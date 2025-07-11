package ru.kropotov.denet.test

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DenetApplication : Application() {

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            initStrictMode()
        }
        super.onCreate()
    }

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(
            ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectCustomSlowCalls()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
    }
}