package com.dscvit.cadence.model.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    val alarmName: String,
    val hour: Int,
    val minute: Int,
    val monday: Boolean,
    val tuesday: Boolean,
    val wednesday: Boolean,
    val thursday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean,
    val isRepeating: Boolean,
    var isOn: Boolean,
    val playlistId: String,
    val songId: String,
    val type: String,
    val songName: String,
    val songArtist: String,
    val songArt: String,
    val songUrl: String,
)
