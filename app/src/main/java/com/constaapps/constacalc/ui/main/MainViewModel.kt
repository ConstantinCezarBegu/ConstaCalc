package com.constaapps.constacalc.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val currentFormula: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val currentAnswer: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val inverse: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
}
