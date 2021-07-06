package com.dscvit.cadence.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dscvit.cadence.model.alarm.Alarm

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm): Long

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarms")
    suspend fun getAllAlarms(): List<Alarm>
}