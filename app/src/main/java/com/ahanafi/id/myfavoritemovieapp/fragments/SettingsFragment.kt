package com.ahanafi.id.myfavoritemovieapp.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ahanafi.id.myfavoritemovieapp.R
import com.ahanafi.id.myfavoritemovieapp.notifications.DailyReleaseNotification
import com.ahanafi.id.myfavoritemovieapp.notifications.DailyReminderNotification

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val dailyReminderNotification = DailyReminderNotification()
    private val dailyReleaseNotification = DailyReleaseNotification()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val changeLanguage = findPreference<Preference>("change_language")
        changeLanguage?.intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when(key) {
            "daily_reminder" -> {
                when(sharedPreferences?.getBoolean(key, false)) {
                    true -> dailyReminderNotification.setDailyReminderAlarm(context)
                    false -> dailyReminderNotification.cancelDailyReminderAlarm(context)
                }
            }
            "daily_release_reminder" -> {
                when(sharedPreferences?.getBoolean(key, false)) {
                    true -> dailyReleaseNotification.setDailyReleaseAlarm(context)
                    false -> dailyReleaseNotification.cancelDailyReleaseAlarm(context)
                }
            }
        }
    }
}