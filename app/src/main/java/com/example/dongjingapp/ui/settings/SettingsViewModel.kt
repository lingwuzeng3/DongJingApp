package com.example.dongjingapp.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dongjingapp.data.repository.SettingsRepository
import com.example.dongjingapp.data.settings.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application,
    private val settingsRepository: SettingsRepository = SettingsRepository(application.applicationContext)
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                _uiState.value = SettingsUiState(
                    themeMode = settings.themeMode,
                    autoPlayVideo = settings.autoPlayVideo,
                    wifiOnlyAutoPlay = settings.wifiOnlyAutoPlay,
                    trainingReminderEnabled = settings.trainingReminderEnabled,
                    reminderHour = settings.reminderHour,
                    reminderMinute = settings.reminderMinute,
                    profileName = settings.profileName,
                    profileEmail = settings.profileEmail,
                    profileAvatar = settings.profileAvatar,
                    profileHeight = settings.profileHeight,
                    profileWeight = settings.profileWeight,
                    profileFitnessGoal = settings.profileFitnessGoal
                )
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { settingsRepository.setThemeMode(mode) }
    }

    fun setAutoPlayVideo(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setAutoPlayVideo(enabled) }
    }

    fun setWifiOnlyAutoPlay(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setWifiOnlyAutoPlay(enabled) }
    }

    fun setTrainingReminderEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setTrainingReminderEnabled(enabled) }
    }

    fun setReminderTime(hour: Int, minute: Int) {
        _uiState.update { it.copy(reminderHour = hour, reminderMinute = minute) }
        viewModelScope.launch { settingsRepository.setReminderTime(hour, minute) }
    }

    fun setProfileName(name: String) {
        viewModelScope.launch { settingsRepository.setProfileName(name) }
    }

    fun setProfileEmail(email: String) {
        viewModelScope.launch { settingsRepository.setProfileEmail(email) }
    }

    fun setProfileAvatar(avatarUri: String) {
        viewModelScope.launch { settingsRepository.setProfileAvatar(avatarUri) }
    }

    fun setProfileBodyData(heightCm: Float?, weightKg: Float?, fitnessGoal: String) {
        viewModelScope.launch {
            settingsRepository.setProfileBodyData(heightCm, weightKg, fitnessGoal)
        }
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(application) as T
                }
            }
    }
}
