package com.dscvit.cadence.alarm

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.widget.Toast
import com.dscvit.cadence.model.alarm.Alarm
import com.dscvit.cadence.repository.AlarmRepository
import com.dscvit.cadence.ui.MainActivity
import com.dscvit.cadence.util.SpotifyConstants
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService : Service() {
    @Inject
    lateinit var repository: AlarmRepository
    var spotifyAppRemote: SpotifyAppRemote? = null
//    private var mediaPlayer: MediaPlayer? = null
//    private var vibrator: Vibrator? = null

    override fun onCreate() {
        super.onCreate()
//        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
//        mediaPlayer!!.isLooping = true
//        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            if (intent.action == "ACTION_STOP_SERVICE") {
                stopForeground(true)
                stopSelf()
                return START_NOT_STICKY
            } else {
                val notificationIntent = Intent(this, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
                val id = intent.getLongExtra("ALARM_ID", -1)
                val now = Calendar.getInstance()
                var notification: Notification?
                if (id >= 0) {
                    runBlocking {
                        val alarm = async { repository.getAlarmById(id) }
                        runBlocking {
                            if (alarm.await().hour == now[Calendar.HOUR_OF_DAY] && alarm.await().minute == now[Calendar.MINUTE]) {
                                notification = sendNotif(
                                    this@AlarmService,
                                    alarm.await(),
                                    pendingIntent
                                )
                                alarm.await().isOn = false
                                repository.updateAlarm(alarm.await())
                                playSong(alarm.await().songId, alarm.await().songUrl)
                                startForeground(id.toInt(), notification)
                            } else {
                                stopSelf()
                            }
                        }
                    }
                }
//        mediaPlayer!!.start()
//        val pattern = longArrayOf(0, 100, 1000)
//        vibrator!!.vibrate(pattern, 0)
            }
        } catch (e: Exception) {
            Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
//        mediaPlayer!!.stop()
//        vibrator!!.cancel()
        if (spotifyAppRemote != null) {
            spotifyAppRemote.let {
                SpotifyAppRemote.disconnect(it)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun playSong(songId: String?, songUrl: String?) {
        val connectionParams = ConnectionParams.Builder(SpotifyConstants.CLIENT_ID)
            .setRedirectUri(SpotifyConstants.REDIRECT_URI)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(
            this,
            connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(appRemote: SpotifyAppRemote) {
                    spotifyAppRemote = appRemote
                    appRemote.playerApi.play(songId)
                }

                override fun onFailure(throwable: Throwable) {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(songUrl)
                    startActivity(i)
                }
            }
        )
    }

    private fun sendNotif(
        context: Context,
        alarm: Alarm,
        pendingIntent: PendingIntent
    ): Notification {
        val stopSelf = Intent(this, AlarmService::class.java)
        stopSelf.action = "ACTION_STOP_SERVICE"
        val cancelIntent =
            PendingIntent.getService(this, 0, stopSelf, PendingIntent.FLAG_CANCEL_CURRENT)
        return NotificationHelper.sendNotification(
            context,
            alarm.alarmName,
            "Now Playing ${alarm.songName}",
            "Now Playing ${alarm.songName} by ${alarm.songArtist}",
            "Alarms",
            (alarm.id?.rem(Int.MAX_VALUE.toLong()))!!.toInt(),
            pendingIntent,
            cancelIntent
        )
    }
}
