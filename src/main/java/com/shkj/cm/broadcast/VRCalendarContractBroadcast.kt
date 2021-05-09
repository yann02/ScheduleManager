package com.shkj.cm.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import com.xuexiang.xutil.tip.ToastUtils

class VRCalendarContractBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        ToastUtils.toast("接收到事件！", Toast.LENGTH_LONG)
        Log.d("dosmono", "接收到事件！")
        val uri = intent.data!!
        val alertTime = uri.lastPathSegment!!
        val selection = CalendarContract.CalendarAlerts.ALARM_TIME + "=?"
        val cursor = context.contentResolver.query(
            CalendarContract.CalendarAlerts.CONTENT_URI_BY_INSTANCE,
            arrayOf(
                CalendarContract.CalendarAlerts.TITLE,
                CalendarContract.CalendarAlerts.DTSTART,
                CalendarContract.CalendarAlerts.DTEND
            ),
            selection,
            arrayOf(alertTime),
            null
        )

        cursor?.apply {
            moveToFirst()
            val title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE))
            val startTime = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTSTART))
            val endTime = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTEND))
            Log.d("dosmono", "time = $title,startTime = $startTime,endTime = $endTime")
        }
    }
}