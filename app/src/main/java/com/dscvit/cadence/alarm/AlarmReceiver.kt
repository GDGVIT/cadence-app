package com.dscvit.cadence.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.dscvit.cadence.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repository: AlarmRepository
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val id = intent?.getLongExtra("ALARM_ID", -1)
            val intentService = Intent(context, AlarmService::class.java)
            intentService.putExtra("ALARM_ID", id)
            val now = Calendar.getInstance()
            runBlocking {
                val alarm = async { repository.getAlarmById(id!!) }
                runBlocking {
                    if (alarm.await().hour == now[Calendar.HOUR_OF_DAY] && alarm.await().minute == now[Calendar.MINUTE]) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startForegroundService(intentService)
                        } else {
                            context.startService(intentService)
                        }
                    }
                }
            }
        }
    }
}
