package com.example.dongjingapp.ui.settings

import com.example.dongjingapp.data.settings.ThemeMode

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val autoPlayVideo: Boolean = true,
    val wifiOnlyAutoPlay: Boolean = false,
    val trainingReminderEnabled: Boolean = false,
    val reminderHour: Int = 20,
    val reminderMinute: Int = 0,
    val profileName: String = "",
    val profileEmail: String = "",
    val profileAvatar: String = "",
    val profileHeight: Float? = null,
    val profileWeight: Float? = null,
    val profileFitnessGoal: String = ""
) {
    val reminderText: String
        get() = String.format("%02d:%02d", reminderHour, reminderMinute)
}
