package com.dscvit.cadence.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dscvit.cadence.model.alarm.Alarm

@Database(
    entities = [Alarm::class],
    version = 1
)
abstract class AlarmDatabase : RoomDatabase() {

    abstract fun getAlarmDao(): AlarmDao

    companion object {
        @Volatile
        private var instance: AlarmDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AlarmDatabase::class.java,
                "alarm_db.db"
            ).build()
    }
}