package com.constaapps.constacalc.util

import androidx.room.Room
import com.constaapps.constacalc.db.ConstaCalcDatabase
import com.constaapps.constacalc.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val ConstaCalcModule = module {
    single {
        Room.databaseBuilder(androidContext(), ConstaCalcDatabase::class.java, "ConstaCalcDatabase.db")
            .build()
    }

    single {
        get<ConstaCalcDatabase>().historyDao()
    }

    viewModel {
        MainViewModel(get())
    }

}