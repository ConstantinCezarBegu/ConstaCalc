package com.constaapps.constacalc

import android.app.Application
import com.constaapps.constacalc.util.ConstaCalcModule
import org.koin.android.ext.android.startKoin

class ConstaCalcApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(ConstaCalcModule))
    }
}