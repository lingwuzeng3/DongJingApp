package com.example.dongjingapp.data.repository

import android.content.Context
import com.example.dongjingapp.data.settings.AppSettings
import com.example.dongjingapp.data.settings.SettingsKeys
import com.example.dongjingapp.data.settings.ThemeMode
import com.example.dongjingapp.data.settings.parseThemeMode
import com.example.dongjingapp.data.settings.settingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit

class SettingsRepository(
    context: Context
) {
    private val dataStore = context.applicationContext.settingsDataStore

    val settingsFlow: Flow<AppSettings> = dataStore.data.map { pref ->
        AppSettings(
            themeMode = parseThemeMode(pref[SettingsKeys.THEME_MODE]),
            autoPlayVideo = pref[SettingsKeys.AUTO_PLAY_VIDEO] ?: true,
            wifiOnlyAutoPlay = pref[SettingsKeys.WIFI_ONLY_AUTO_PLAY] ?: false,
            trainingReminderEnabled = pref[SettingsKeys.TRAINING_REMINDER_ENABLED] ?: false,
            reminderHour = pref[SettingsKeys.REMINDER_HOUR] ?: 20,
            reminderMinute = pref[SettingsKeys.REMINDER_MINUTE] ?: 0,
            profileName = pref[SettingsKeys.PROFILE_NAME] ?: "",
            profileEmail = pref[SettingsKeys.PROFILE_EMAIL] ?: "",
            profileAvatar = pref[SettingsKeys.PROFILE_AVATAR] ?: "",
            profileHeight = pref[SettingsKeys.PROFILE_HEIGHT],
            profileWeight = pref[SettingsKeys.PROFILE_WEIGHT],
            profileFitnessGoal = pref[SettingsKeys.PROFILE_FITNESS_GOAL] ?: ""
        )
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { it[SettingsKeys.THEME_MODE] = mode.name }
    }

    suspend fun setAutoPlayVideo(enabled: Boolean) {
        dataStore.edit { it[SettingsKeys.AUTO_PLAY_VIDEO] = enabled }
    }

    suspend fun setWifiOnlyAutoPlay(enabled: Boolean) {
        dataStore.edit { it[SettingsKeys.WIFI_ONLY_AUTO_PLAY] = enabled }
    }

    suspend fun setTrainingReminderEnabled(enabled: Boolean) {
        dataStore.edit { it[SettingsKeys.TRAINING_REMINDER_ENABLED] = enabled }
    }

    suspend fun setReminderTime(hour: Int, minute: Int) {
        dataStore.edit {
            it[SettingsKeys.REMINDER_HOUR] = hour.coerceIn(0, 23)
            it[SettingsKeys.REMINDER_MINUTE] = minute.coerceIn(0, 59)
        }
    }

    suspend fun setProfileName(name: String) {
        dataStore.edit { it[SettingsKeys.PROFILE_NAME] = name.trim() }
    }

    suspend fun setProfileEmail(email: String) {
        dataStore.edit { it[SettingsKeys.PROFILE_EMAIL] = email.trim() }
    }

    suspend fun setProfileAvatar(avatarUri: String) {
        dataStore.edit { it[SettingsKeys.PROFILE_AVATAR] = avatarUri.trim() }
    }

    suspend fun setProfileBodyData(
        heightCm: Float?,
        weightKg: Float?,
        fitnessGoal: String
    ) {
        dataStore.edit {
            if (heightCm == null) it.remove(SettingsKeys.PROFILE_HEIGHT)
            else it[SettingsKeys.PROFILE_HEIGHT] = heightCm
            if (weightKg == null) it.remove(SettingsKeys.PROFILE_WEIGHT)
            else it[SettingsKeys.PROFILE_WEIGHT] = weightKg
            it[SettingsKeys.PROFILE_FITNESS_GOAL] = fitnessGoal.trim()
        }
    }
}
