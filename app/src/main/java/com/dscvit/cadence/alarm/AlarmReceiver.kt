package com.dscvit.cadence.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.dscvit.cadence.model.alarm.Alarm
import com.dscvit.cadence.repository.AlarmRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val id = intent?.getLongExtra("ALARM_ID", -1)
            val songId = intent?.getStringExtra("SONG_ID")
            val songName = intent?.getStringExtra("SONG_NAME")
            val songArtist = intent?.getStringExtra("SONG_ARTIST")
            val songArt = intent?.getStringExtra("SONG_ART")
            val songUrl = intent?.getStringExtra("SONG_URL")
            Toast.makeText(context, "Start", Toast.LENGTH_SHORT).show()
            val intentService = Intent(context, AlarmService::class.java)
            intentService.putExtra("ALARM_ID", id)
            intentService.putExtra("SONG_ID", songId)
            intentService.putExtra("SONG_NAME", songName)
            intentService.putExtra("SONG_ARTIST",songArtist)
            intentService.putExtra("SONG_ART",songArt)
            intentService.putExtra("SONG_URL", songUrl)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intentService)
            } else {
                context.startService(intentService)
            }

        }
    }
}
