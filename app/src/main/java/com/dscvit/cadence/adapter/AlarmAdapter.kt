package com.dscvit.cadence.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dscvit.cadence.R
import com.dscvit.cadence.model.alarm.Alarm
import com.google.android.material.switchmaterial.SwitchMaterial

class AlarmAdapter(
    private var alarms: List<Alarm>
) :
    RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val alarmName: TextView = view.findViewById(R.id.alarmName)
        val alarmTime: TextView = view.findViewById(R.id.alarmTime)
        val alarmSwitch: SwitchMaterial = view.findViewById(R.id.alarmSwitch)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_alarm, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val alarm = alarms[position]

        viewHolder.apply {
            alarmName.text = alarm.alarmName
            alarmSwitch.isChecked = alarm.isOn
            alarmTime.text = "${alarm.hour}:${alarm.minute}"
        }
    }

    fun dataSetChange(newAlarms: List<Alarm>) {
        alarms = newAlarms
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = alarms.size

}