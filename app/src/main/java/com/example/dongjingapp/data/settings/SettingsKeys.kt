package com.example.dongjingapp.data.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsKeys {
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val AUTO_PLAY_VIDEO = booleanPreferencesKey("auto_play_video")
    val WIFI_ONLY_AUTO_PLAY = booleanPreferencesKey("wifi_only_auto_play")
    val TRAINING_REMINDER_ENABLED = booleanPreferencesKey("training_reminder_enabled")
    val REMINDER_HOUR = intPreferencesKey("reminder_hour")
    val REMINDER_MINUTE = intPreferencesKey("reminder_minute")
    val PROFILE_NAME = stringPreferencesKey("profile_name")
    val PROFILE_EMAIL = stringPreferencesKey("profile_email")
    val PROFILE_AVATAR = stringPreferencesKey("profile_avatar")
    val PROFILE_HEIGHT = floatPreferencesKey("profile_height")
    val PROFILE_WEIGHT = floatPreferencesKey("profile_weight")
    val PROFILE_FITNESS_GOAL = stringPreferencesKey("profile_fitness_goal")
}
