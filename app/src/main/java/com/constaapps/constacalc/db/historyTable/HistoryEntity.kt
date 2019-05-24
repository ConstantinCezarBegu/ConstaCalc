package com.constaapps.constacalc.db.historyTable

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "history")
data class HistoryEntity(
    val formula: String,
    val answer: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}