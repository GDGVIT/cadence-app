package com.dscvit.cadence.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dscvit.cadence.model.alarm.Alarm

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: Alarm): Long

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Query("DELETE FROM alarms WHERE id=:alarmId")
    suspend fun deleteAlarm(alarmId: Long)

    @Query("SELECT * FROM alarms")
    suspend fun getAllAlarms(): List<Alarm>

    @Query("SELECT * FROM alarms where id=:alarmId")
    suspend fun getAlarmById(alarmId: Long): Alarm
}
