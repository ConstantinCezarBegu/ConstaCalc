package com.constaapps.constacalc.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.constaapps.constacalc.db.historyTable.HistoryDao
import com.constaapps.constacalc.db.historyTable.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class ConstaCalcDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}