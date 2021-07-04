package com.dscvit.cadence.model.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val alarmName: String,
    val hour: Int,
    val minute: Int,
    val days: Days,
    val playlistId: String,
    val songId: String
)
