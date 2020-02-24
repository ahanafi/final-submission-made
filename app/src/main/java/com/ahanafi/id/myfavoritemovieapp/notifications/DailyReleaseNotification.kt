package com.ahanafi.id.myfavoritemovieapp.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ahanafi.id.myfavoritemovieapp.BuildConfig
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.utils.setNotification
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class DailyReleaseNotification : BroadcastReceiver() {

    companion object{
        private var DAILY_RELEASE = 200
        private var CHANNEL_ID = 220
        var EXTRA_TITLE = "extra_title"
        var EXTRA_MESSAGE = "extra_message"
        private const val DAILY_RELEASE_CHANNEL = "Daily Release Movie"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val title : String? = context.getString(R.string.daily_release_movie)
        var message : String?

        val client = AsyncHttpClient()
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(date)

        val url = "https://api.themoviedb.org/3/discover/movie?api_key=${BuildConfig.TheMovieDB_ApiKey}&primary_release_date.gte=$currentDate&primary_release_date.lte=$currentDate"
        client.get(url, object : AsyncHttpResponseHandler() {
            @SuppressLint("DefaultLocale")
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val responseData = responseObject.getJSONArray("results")
                    for (i in 0 until responseData.length()) {
                        val movieData = responseData.getJSONObject(i)
                        CHANNEL_ID += i
                        val movieTitle = movieData.getString("title")
                        val overview = movieData.getString("overview")
                        message = movieTitle.toUpperCase() + " : " + overview

                        setNotification(context, title, message, CHANNEL_ID, DAILY_RELEASE_CHANNEL)
                    }
                } catch (e: Exception) {
                    Log.d("Exception: ", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d("onFailure", error?.message.toString())
            }
        })
    }

    fun setDailyReleaseAlarm(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmTime = Calendar.getInstance().apply{
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            timeInMillis = System.currentTimeMillis()
        }

        val today = Calendar.getInstance()
        val title : String? = context.getString(R.string.daily_release_reminder)
        val message : String? = context.getString(R.string.daily_release_notif_message)
        val intent = Intent(context, DailyReleaseNotification::class.java)
        intent.putExtra(EXTRA_TITLE, title)
        intent.putExtra(EXTRA_MESSAGE, message)
        val pendingIntent = PendingIntent.getBroadcast(context,
            DAILY_RELEASE, intent, 0)

        if(alarmTime.before(today)) {
            alarmTime.add(Calendar.HOUR_OF_DAY, 24)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmTime.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
    }

    fun cancelDailyReleaseAlarm(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReleaseNotification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,
            DAILY_RELEASE, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
}
