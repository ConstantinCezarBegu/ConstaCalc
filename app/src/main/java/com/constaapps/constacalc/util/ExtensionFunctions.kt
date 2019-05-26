package com.constaapps.constacalc.util

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.constaapps.constacalc.ui.main.MainViewModel

fun List<String>.convertAndClean(mainViewModel: MainViewModel): String {
    val radOrDegree =
        if (mainViewModel.degree.value!!) {
            this.toString()
                .replace("sin(", "sdeg(")
                .replace("cos(", "cdeg(")
                .replace("tan(", "tdeg(")
                .replace("sin-1(", "s-1deg(")
                .replace("cos-1(", "c-1deg(")
                .replace("tan-1(", "t-1deg(")
        } else {
            this.toString()
                .replace("sdeg(", "sin(")
                .replace("cdeg(", "cos(")
                .replace("tdeg(", "tan(")
                .replace("s-1deg(", "sin-1(")
                .replace("c-1deg(", "cos-1(")
                .replace("t-1deg(", "tan-1(")
        }
    return radOrDegree.replace(" ", "").replace(",", "").dropLast(1).drop(1)
}


fun List<String>.cleanListToString(): String {
    return this.toString().replace(" ", "").replace(",", "").dropLast(1).drop(1)
}

fun MutableLiveData<MutableList<String>>.update(newVal: String) {
    this.value?.add(newVal)
    this.value = this.value
}

fun MutableLiveData<MutableList<String>>.delete() {
    if (!this.value.isNullOrEmpty()) {
        this.value?.removeAt(this.value!!.lastIndex)
        this.value = this.value
    }

}

fun MutableLiveData<MutableList<String>>.clear() {
    if (!this.value.isNullOrEmpty()) {
        this.value?.clear()
        this.value = this.value
    }
}

