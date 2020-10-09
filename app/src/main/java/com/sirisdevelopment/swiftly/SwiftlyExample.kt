package com.sirisdevelopment.swiftly

import android.app.Application
import android.content.Context
import com.sirisdevelopment.swiftly.data.SwiftlyData
import timber.log.Timber

class SwiftlyExample : Application() {

    companion object {
        lateinit var context : Context
        lateinit var swiftlyData : SwiftlyData
    }

    override fun onCreate() {
        super.onCreate()
        context = super.getApplicationContext();
        Timber.plant(Timber.DebugTree());
        swiftlyData = SwiftlyData(context)
    }
}