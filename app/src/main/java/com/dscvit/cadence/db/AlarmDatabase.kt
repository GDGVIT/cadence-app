package com.dscvit.cadence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dscvit.cadence.model.alarm.Alarm

@Database(
    entities = [Alarm::class],
    version = 1
)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun getAlarmDao(): AlarmDao
}
