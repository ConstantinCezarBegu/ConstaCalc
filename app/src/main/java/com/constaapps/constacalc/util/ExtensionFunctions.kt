package com.constaapps.constacalc.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.constaapps.constacalc.recyclerview.SwipeToDeleteCallback
import com.constaapps.constacalc.ui.main.MainViewModel

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


fun ViewGroup.inflate(
    @LayoutRes layoutId: Int,
    inflater: LayoutInflater = LayoutInflater.from(context),
    attachToRoot: Boolean = false
): View {
    return inflater.inflate(layoutId, this, attachToRoot)
}

fun RecyclerView.attachSwipeHandler(swipeHandler: SwipeToDeleteCallback) {
    val itemTouchHelper = ItemTouchHelper(swipeHandler)
    itemTouchHelper.attachToRecyclerView(this)
}

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


fun String.isNumber(): Boolean {
    return this.matches(".?-?((\\d*\\.\\d+)|\\d+\\.?)(E\\d+)?".toRegex())
}

fun String.isCharacterNumber(): Boolean {
    return this == "PI" || this == "e" || this == "*PI" || this == "*e"
}

fun String.isSpecialCase(): Boolean {
    return this == ")" || this == "percentage"
}

fun String.smartNumber(last: String): String {
    return if ((this == "PI" || this == "e") && (last.isSpecialCase() || last.isNumber() || last.isCharacterNumber())) {
        "*$this"
    } else if (this.isNumber() && (last.isSpecialCase() || last.isCharacterNumber())) {
        "*$this"
    } else {
        this
    }
}

fun String.smartFunction(last: String, isDisplay: Boolean): String {
    return if (last.isNumber() || last.isSpecialCase() || last.isCharacterNumber()) {
        "${if (isDisplay) "×" else "*"}$this"
    } else {
        this
    }
}

fun smartRoot(last: String, isDisplay: Boolean): String {
    return if (last.isNumber() || last.isCharacterNumber()) {
        if (isDisplay) "√(" else "root("
    } else if (last.isSpecialCase()) {
        if (isDisplay) "×√(" else "*sqrt("
    } else {
        if (isDisplay) "√(" else "sqrt("
    }
}

fun smartNegative(last: String): String {
    return if (last.isCharacterNumber() || last.isSpecialCase() || last.isNumber()) {
        "minus"
    } else {
        "-"
    }
}