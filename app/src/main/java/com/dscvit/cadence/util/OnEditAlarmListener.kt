package com.dscvit.cadence.util

import com.dscvit.cadence.model.alarm.Alarm

interface OnEditAlarmListener {
    fun onToggle(alarm: Alarm)
    fun onDelete(alarm: Alarm)
}