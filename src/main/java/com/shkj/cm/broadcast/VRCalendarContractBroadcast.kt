package com.shkj.cm.broadcast

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.CalendarContract
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.dosmono.platecommon.util.UIUtils
import com.orhanobut.logger.Logger
import com.shkj.cm.R
import com.xuexiang.xutil.tip.ToastUtils
import java.lang.Exception


class VRCalendarContractBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        ToastUtils.toast(
            "接收到" +
                    "事件！", Toast.LENGTH_LONG
        )
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

        try {
            cursor?.apply {
                moveToFirst()
                val title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE))
                val startTime = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTSTART))
                val endTime = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DTEND))
                Log.d("dosmono", "time = $title,startTime = $startTime,endTime = $endTime")
                showDialog(title)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun showDialog(msg: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(UIUtils.getContext())

        val alertDialog: AlertDialog = builder.create()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        } else {
            alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        }
        val view: View = alertDialog.layoutInflater.inflate(R.layout.alert_dialog, null)
        val tvMessage: TextView = view.findViewById(R.id.tv_alert_message)
        tvMessage.text = msg
        builder.setView(view)
        alertDialog.setView(view, 0, 0, 0, 0)
        val wlp: WindowManager.LayoutParams = alertDialog.window?.attributes!!
        wlp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        wlp.y = 1143
        alertDialog.show()
        alertDialog.window?.setLayout(780, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.findViewById<TextView>(R.id.tv_positive).setOnClickListener {
            alertDialog.dismiss()
        }
    }
}