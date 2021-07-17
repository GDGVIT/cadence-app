package com.dscvit.cadence.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val id = intent?.getLongExtra("ALARM_ID", -1)
            val intentService = Intent(context, AlarmService::class.java)
            intentService.putExtra("ALARM_ID", id)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intentService)
            } else {
                context.startService(intentService)
            }
        }
    }
}
