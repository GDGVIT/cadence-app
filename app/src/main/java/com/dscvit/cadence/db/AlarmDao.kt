package com.dscvit.cadence.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dscvit.cadence.model.alarm.Alarm

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(alarm: Alarm): Long

    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): LiveData<List<Alarm>>

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)
}