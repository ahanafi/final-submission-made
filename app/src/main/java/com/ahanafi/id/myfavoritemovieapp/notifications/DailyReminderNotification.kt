package com.ahanafi.id.myfavoritemovieapp.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.utils.setNotification
import java.util.*

class DailyReminderNotification : BroadcastReceiver() {

    companion object{
        const val DAILY_REMINDER = 100
        const val CHANNEL_ID = 110
        const val DAILY_NOTIFICATION_CHANNEL = "Daily Reminder"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_message"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra(EXTRA_TITLE)
        val message = intent?.getStringExtra(EXTRA_MESSAGE)

        setNotification(context, title, message, CHANNEL_ID, DAILY_NOTIFICATION_CHANNEL)
    }

    fun setDailyReminderAlarm(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmTime = Calendar.getInstance().apply{
            set(Calendar.HOUR_OF_DAY, 7)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            timeInMillis = System.currentTimeMillis()
        }

        val today = Calendar.getInstance()
        val title : String? = context.getString(R.string.daily_reminder_notif)
        val message : String? = context.getString(R.string.daily_reminder_notif_message)
        val intent = Intent(context, DailyReminderNotification::class.java)
        intent.putExtra(EXTRA_TITLE, title)
        intent.putExtra(EXTRA_MESSAGE, message)
        val pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER, intent, 0)

        if(alarmTime.before(today)) {
            alarmTime.add(Calendar.HOUR_OF_DAY, 24)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
    }

    fun cancelDailyReminderAlarm(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminderNotification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

}
