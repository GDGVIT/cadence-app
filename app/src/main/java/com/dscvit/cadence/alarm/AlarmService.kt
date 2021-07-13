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
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService : Service() {
    @Inject
    lateinit var repository: AlarmRepository

    lateinit var spotifyAppRemote: SpotifyAppRemote

    //    private var mediaPlayer: MediaPlayer? = null
//    private var vibrator: Vibrator? = null
    override fun onCreate() {
        super.onCreate()
//        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
//        mediaPlayer!!.isLooping = true
//        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val id = intent.getLongExtra("ALARM_ID", -1)
        val songId = intent.getStringExtra("SONG_ID")
        val songName = intent.getStringExtra("SONG_NAME")
        val songArtist = intent.getStringExtra("SONG_ARTIST")
        val songArt = intent.getStringExtra("SONG_ART")
        val songUrl = intent.getStringExtra("SONG_URL")
        Toast.makeText(this, "SERVICE", Toast.LENGTH_SHORT).show()
        var notification: Notification? = null
        if (id >= 0) {
            runBlocking {
                val alarm = async { repository.getAlarmById(id) }
                runBlocking {
                    notification = sendNotif(
                        this@AlarmService,
                        alarm.await(),
                        songId,
                        songName,
                        songArtist,
                        pendingIntent
                    )
                }
            }
        }
        playSong(songId, songUrl)
//        mediaPlayer!!.start()
//        val pattern = longArrayOf(0, 100, 1000)
//        vibrator!!.vibrate(pattern, 0)
        startForeground(id.toInt(), notification)
        stopForeground(false)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
//        mediaPlayer!!.stop()
//        vibrator!!.cancel()
        spotifyAppRemote.let {
            SpotifyAppRemote.disconnect(it)
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
                    // Something went wrong when attempting to connect! Handle errors here
                }
            }
        )
    }

    private fun sendNotif(
        context: Context,
        alarm: Alarm,
        songId: String?,
        songName: String?,
        songArtist: String?,
        pendingIntent: PendingIntent
    ): Notification {
        Toast.makeText(context, "ALARM TIME ${alarm.id} $songName", Toast.LENGTH_LONG).show()
        return NotificationHelper.sendNotification(
            context,
            alarm.alarmName,
            "Now Playing $songName",
            "Now Playing $songName by $songArtist",
            "Alarms",
            (alarm.id?.rem(Int.MAX_VALUE.toLong()))!!.toInt(),
            pendingIntent
        )
    }
}
