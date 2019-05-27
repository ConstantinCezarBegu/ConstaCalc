package com.constaapps.constacalc.db.historyTable

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAll(history: List<HistoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(history: HistoryEntity)

    @Update
    fun update(history: HistoryEntity)

    @Delete
    fun delete(history: HistoryEntity)

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun findAll(): LiveData<List<HistoryEntity>>


    @Query("DELETE FROM history")
    fun dropTable()

    @Query("SELECT * FROM history WHERE isValid = 1 ORDER BY id DESC")
    suspend fun findLatestCorrectHistory() : List<HistoryEntity>
}