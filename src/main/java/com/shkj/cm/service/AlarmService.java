package com.shkj.cm.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


import com.shkj.cm.calendarview.utils.CalendarUtil;
import com.shkj.cm.common.Constant;
import com.shkj.cm.common.RepeatType;
import com.xuexiang.xutil.data.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AlarmService extends Service {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent startNotification;
    private static final String TAG = "service";
    private static final String KEY_RINGTONE = "ring_tone";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "服务启动！");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTimeInMillis(System.currentTimeMillis());


//        startNotification = new Intent(AlarmService.this, AlarmReceiver.class);   //启动广播
//        startNotification.putExtra("title", todos.getTitle());
//        startNotification.putExtra("dsc", todos.getDesc());
//        startNotification.putExtra("ringTone", (String) SPUtils.get(getApplication(), KEY_RINGTONE, ""));
//        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);   //这里是系统闹钟的对象
//        pendingIntent = PendingIntent.getBroadcast(this, todos.getId(), startNotification, PendingIntent.FLAG_UPDATE_CURRENT);   //设置事件
//        alarmManager.set(AlarmManager.RTC_WAKEUP, todos.getRemindTime(), pendingIntent);    //提交事件，发送给 广播接收器,提醒一次
//        Log.i(TAG, "发送单次提醒");
//        Log.i(TAG, "标题是:" + todos.getTitle());
//        Log.i(TAG, "时间是:" + todos.getRemindTime());
//        Log.i(TAG, "日期是:" + System.currentTimeMillis() / 1000 / 60 / 60 / 24);
//        Log.i(TAG, "铃声：" + (String) SPUtils.get(getApplication(), KEY_RINGTONE, ""));
//        //设置每隔24小时提醒一次
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, todos.getRemindTimeNoDay(), 1000 * 60 * 60 * 24, pendingIntent);
//        Log.i(TAG, "发送重复提醒");
//        Log.i(TAG, "标题是:" + todos.getTitle());
//        Log.i(TAG, "时间是:" + todos.getRemindTimeNoDay());
//        Log.i(TAG, "日期是:" + System.currentTimeMillis() / 1000 / 60 / 60 / 24);
//        return START_REDELIVER_INTENT;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void handleTime(long startTime, long endTime, RepeatType repeat, long[] frequencys) {

        long[] afterFrequencys = new long[frequencys.length];
        Date startDate = DateUtils.millis2Date(startTime);
        Date endDate = DateUtils.millis2Date(endTime);
        for (int i = 0; i < frequencys.length; i++) {
            afterFrequencys[i] = (startTime - frequencys[i]);
        }
        if (repeat == RepeatType.No_repeat) {

        } else if (repeat == RepeatType.Every_day) {
            for (int i = 0; i < frequencys.length; i++) {
                afterFrequencys[i] = afterFrequencys[i] + Constant.DAY_INTERVAL;

            }
        } else if (repeat == RepeatType.Every_week) {
            for (int i = 0; i < frequencys.length; i++) {
                afterFrequencys[i] = afterFrequencys[i] + Constant.WEEK_INTERVAL;
            }
        } else if (repeat == RepeatType.Every_month) {

            for (int i = 0; i < frequencys.length; i++) {
                Date tempStartDate = DateUtils.millis2Date(afterFrequencys[i]);
                //开始日期的 天数
                int dayOfTempStartDate = DateUtils.getDay(tempStartDate);
                //结束日期的天数
                int dayOfEndDate = DateUtils.getDay(endDate);
                //结束日期的月份
                int monthOfEndDate = DateUtils.getMonth(endDate);
                //开始日期的月份
                int monthOfTempStartDate = DateUtils.getMonth(tempStartDate);
                //开始日期的年份
                int yearOfStartDate = DateUtils.getYear(tempStartDate);
                //结束日期 当月的最大天数
                int maxDayOfEndDate = getMaxDayByYearMonth(yearOfStartDate, monthOfEndDate);
                Calendar tempStartCalendar = Calendar.getInstance();
                Calendar endCalendar = Calendar.getInstance();
                tempStartCalendar.setTime(tempStartDate);
                endCalendar.setTime(endDate);
                //小时
                int hourOfTempStartDate = tempStartCalendar.get(Calendar.HOUR);
                int hourOfEndDate = endCalendar.get(Calendar.HOUR);
                //分钟
                int minuteOfTempStartDate = tempStartCalendar.get(Calendar.MINUTE);
                int minuteOfEndStartDate = endCalendar.get(Calendar.MINUTE);
                //不设置的情况
                if (dayOfTempStartDate > maxDayOfEndDate || dayOfTempStartDate > dayOfEndDate) {
                    afterFrequencys[i] = 0;
                    continue;
                } else if (dayOfTempStartDate == dayOfEndDate && hourOfTempStartDate > hourOfEndDate) {
                    afterFrequencys[i] = 0;
                    continue;
                } else if (dayOfTempStartDate == dayOfEndDate && hourOfTempStartDate == hourOfEndDate && minuteOfTempStartDate > minuteOfEndStartDate) {
                    afterFrequencys[i] = 0;
                    continue;
                }
                tempStartCalendar.add(Calendar.MONTH, 1);
                afterFrequencys[i] = tempStartCalendar.getTimeInMillis();
            }

        } else if (repeat == RepeatType.Every_year) {

        }

    }

    public int getMaxDayByYearMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        return calendar.getActualMaximum(Calendar.DATE);
    }
}

