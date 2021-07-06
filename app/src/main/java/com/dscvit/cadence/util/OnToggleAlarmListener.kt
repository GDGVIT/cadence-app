package com.dscvit.cadence.util

import com.dscvit.cadence.model.alarm.Alarm

interface OnToggleAlarmListener {
    fun onToggle(alarm: Alarm)
}