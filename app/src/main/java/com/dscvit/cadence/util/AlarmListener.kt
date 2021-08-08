package com.dscvit.cadence.util

import com.dscvit.cadence.model.alarm.Alarm

interface AlarmListener {
    fun onToggle(alarm: Alarm)
    fun onDelete(alarm: Alarm)
    fun onEdit(alarm: Alarm)
}
