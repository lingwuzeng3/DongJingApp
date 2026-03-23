package com.example.dongjingapp

import com.example.dongjingapp.data.settings.ThemeMode
import com.example.dongjingapp.data.settings.parseThemeMode
import com.example.dongjingapp.ui.profile.calculateBMI
import com.example.dongjingapp.ui.settings.SettingsUiState
import org.junit.Assert.assertEquals
import org.junit.Test

class SettingsAndProfileUnitTest {

    @Test
    fun parseThemeMode_returnsSystemWhenInvalid() {
        assertEquals(ThemeMode.SYSTEM, parseThemeMode("UNKNOWN"))
        assertEquals(ThemeMode.SYSTEM, parseThemeMode(null))
    }

    @Test
    fun parseThemeMode_returnsExpectedEnum() {
        assertEquals(ThemeMode.LIGHT, parseThemeMode("LIGHT"))
        assertEquals(ThemeMode.DARK, parseThemeMode("DARK"))
    }

    @Test
    fun reminderText_formatsAsHHmm() {
        val uiState = SettingsUiState(reminderHour = 9, reminderMinute = 5)
        assertEquals("09:05", uiState.reminderText)
    }

    @Test
    fun calculateBMI_returnsPlaceholderWhenDataMissing() {
        assertEquals("--", calculateBMI(null, 60f))
        assertEquals("--", calculateBMI(175f, null))
    }

    @Test
    fun reminderText_stillFormatsAfterProfileFieldsAdded() {
        val uiState = SettingsUiState(
            reminderHour = 23,
            reminderMinute = 59,
            profileName = "新昵称",
            profileEmail = "new@example.com"
        )
        assertEquals("23:59", uiState.reminderText)
    }
}
