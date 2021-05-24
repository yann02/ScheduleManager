package com.shkj.cm.broadcast

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.provider.CalendarContract
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.dosmono.platecommon.util.PreferencesUtil
import com.dosmono.platecommon.util.UIUtils
import com.shkj.cm.R


class VRCalendarContractBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val uri = intent.data!!
        val alertTime = uri.lastPathSegment!!
        val selection = CalendarContract.CalendarAlerts.ALARM_TIME + "=?"
        context.contentResolver.query(
            CalendarContract.CalendarAlerts.CONTENT_URI_BY_INSTANCE,
            arrayOf(
                CalendarContract.CalendarAlerts.TITLE,
                CalendarContract.CalendarAlerts.DTSTART,
                CalendarContract.CalendarAlerts.DTEND,
                CalendarContract.CalendarAlerts.DESCRIPTION
            ),
            selection,
            arrayOf(alertTime),
            null
        ).use { cursor ->
            cursor?.apply {
                moveToFirst()
                for (i in 1..count) {
                    val title = getString(getColumnIndex(CalendarContract.Events.TITLE))
//                    val startTime = getString(getColumnIndex(CalendarContract.Events.DTSTART))
//                    val endTime = getString(getColumnIndex(CalendarContract.Events.DTEND))
                    val description = getString(getColumnIndex(CalendarContract.Events.DESCRIPTION))
                    val monoid = PreferencesUtil.getMonoid(UIUtils.getContext())?.monoid
                    if (!monoid.isNullOrEmpty()) {
                        if (description.equals(monoid)) {
                            //  只对当前用户弹出提醒
                            showDialog(title)
                        }
                    }
                    if (i < count) {
                        moveToNext()
                    }
                }
            }
        }
    }

    private val ringtonePath = "/system/media/audio/alarms/Alarm_Classic.ogg"
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
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
        var mediaPlayer = MediaPlayer().apply {
            setDataSource(ringtonePath)
            prepare()
            start()
        }
        alertDialog.window?.setLayout(780, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.findViewById<TextView>(R.id.tv_positive).setOnClickListener {
            alertDialog.dismiss()
            mediaPlayer.stop()
        }

    }
}