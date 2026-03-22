package com.example.dongjingapp.ui.stats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dongjingapp.data.repository.TrainingRepository
import com.example.dongjingapp.util.weekStartMillis
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(
    application: Application,
    private val trainingRepository: TrainingRepository = TrainingRepository(application.applicationContext)
) : AndroidViewModel(application) {

    private val _localWeekMinutes = MutableStateFlow(0)
    val localWeekMinutes: StateFlow<Int> = _localWeekMinutes.asStateFlow()

    init {
        refreshLocalWeek()
    }

    fun refreshLocalWeek() {
        viewModelScope.launch {
            val sec = trainingRepository.totalDurationSecondsSince(weekStartMillis())
            _localWeekMinutes.value = (sec / 60L).toInt()
        }
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return StatsViewModel(application) as T
                }
            }
    }
}
