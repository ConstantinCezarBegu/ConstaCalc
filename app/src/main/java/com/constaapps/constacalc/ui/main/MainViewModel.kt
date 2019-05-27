package com.constaapps.constacalc.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.constaapps.constacalc.db.historyTable.HistoryDao
import com.constaapps.constacalc.db.historyTable.HistoryEntity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel(private val historyDao: HistoryDao) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    var allowEquals = false
    var allowDecimal = true
    var historyDisplay = false

    val grammarFormula = MutableLiveData<MutableList<String>>().apply { value = mutableListOf() }
    val displayFormula = MutableLiveData<MutableList<String>>().apply { value = mutableListOf() }

    val currentAnswer: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply { this.value = "0" }
    }

    val degree: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { this.value = false }
    }

    val inverse: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { this.value = false }
    }

    fun getAllHistory(): LiveData<List<HistoryEntity>> {
        return historyDao.findAll()
    }

    fun getANS() = runBlocking {
        withContext(Dispatchers.Default) { historyDao.findLatestCorrectHistory() }
    }

    fun saveHistory(historyEntity: HistoryEntity) {
        launch { historyDao.save(historyEntity) }
    }

    fun deleteHistory(historyEntity: HistoryEntity) {
        launch { historyDao.delete(historyEntity) }
    }

    fun deleteAll() {
        launch { historyDao.dropTable() }
    }

}
