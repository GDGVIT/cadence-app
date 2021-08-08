package com.dscvit.cadence.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dscvit.cadence.repository.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class DeviceBootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repository: AlarmRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action.equals(
                "android.intent.action.BOOT_COMPLETED"
            ) ||
            intent.action.equals(
                    "android.intent.action.MY_PACKAGE_REPLACED"
                )
        ) {
            runBlocking {
                val alarms = async { repository.getAllAlarms() }
                runBlocking {
                    for (alarm in alarms.await()) {
                        if (alarm.isOn) {
                            val id = alarm.id
                            if (id != null) {
                                val alarmManager =
                                    context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                val i = Intent(context, AlarmReceiver::class.java)
                                i.putExtra("ALARM_ID", id)
                                val pi = PendingIntent.getBroadcast(context, id.toInt(), i, 0)

                                val now = Calendar.getInstance()
                                val schedule = now.clone() as Calendar
                                schedule[Calendar.HOUR_OF_DAY] = alarm.hour
                                schedule[Calendar.MINUTE] = alarm.minute
                                schedule[Calendar.SECOND] = 0
                                schedule[Calendar.MILLISECOND] = 0

                                val recList = listOf(
                                    alarm.sunday,
                                    alarm.monday,
                                    alarm.tuesday,
                                    alarm.wednesday,
                                    alarm.thursday,
                                    alarm.friday,
                                    alarm.saturday,
                                )

                                if (!recList.contains(true)) {
                                    if (schedule <= now) schedule.add(Calendar.DATE, 1)
                                    setAlarm(pi, schedule, alarmManager)
                                } else {
                                    var alarmSet = false

                                    for (
                                        idx in
                                        now[Calendar.DAY_OF_WEEK] - 1..now[Calendar.DAY_OF_WEEK] + 5
                                    ) {
                                        val idx2 = idx % 7 + 1
                                        if (recList[idx2 - 1]) {
                                            if (schedule > now) {
                                                setAlarm(pi, schedule, alarmManager)
                                                alarmSet = true
                                                break
                                            }
                                        }
                                        schedule.add(Calendar.DATE, 1)
                                    }
                                    if (!alarmSet) {
                                        setAlarm(pi, schedule, alarmManager)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setAlarm(pi: PendingIntent, schedule: Calendar, alarmManager: AlarmManager) {
        val info = AlarmManager.AlarmClockInfo(schedule.timeInMillis, pi)
        alarmManager.setAlarmClock(info, pi)
    }
}
