package com.example.dongjingapp.data.settings

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

fun parseThemeMode(raw: String?): ThemeMode {
    if (raw.isNullOrBlank()) return ThemeMode.SYSTEM
    return runCatching { ThemeMode.valueOf(raw) }.getOrDefault(ThemeMode.SYSTEM)
}

data class AppSettings(
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
)
