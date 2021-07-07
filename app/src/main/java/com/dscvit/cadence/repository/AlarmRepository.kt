package com.dscvit.cadence.repository

import com.dscvit.cadence.db.AlarmDao
import com.dscvit.cadence.model.alarm.Alarm
import javax.inject.Inject

class AlarmRepository @Inject constructor(
    private val alarmDao: AlarmDao
) {
    suspend fun insertAlarm(alarm: Alarm): Long = alarmDao.insertAlarm(alarm)
    suspend fun updateAlarm(alarm: Alarm) = alarmDao.updateAlarm(alarm)
    suspend fun deleteAlarm(alarmId: Long) = alarmDao.deleteAlarm(alarmId)
    suspend fun getAllAlarms(): List<Alarm> = alarmDao.getAllAlarms()
}