package com.constaapps.constacalc.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val currentFormula = MutableLiveData<MutableList<String>>().apply { value = mutableListOf() }
    val displayFormula = MutableLiveData<MutableList<String>>().apply { value = mutableListOf() }

    val currentAnswer: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply { this.value = "0" }
    }

    val inverse: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { this.value = false }
    }

    val allowDecimal: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { this.value = true }
    }

    val degree: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { this.value = false }
    }

}
