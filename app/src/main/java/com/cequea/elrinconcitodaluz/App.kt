package com.cequea.elrinconcitodaluz
import android.app.Application
import com.cequea.elrinconcitodaluz.utils.logging.CrashAndLog
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App: Application(){
    override fun onCreate() {
        super.onCreate()
        CrashAndLog.setupTimber()
    }
}