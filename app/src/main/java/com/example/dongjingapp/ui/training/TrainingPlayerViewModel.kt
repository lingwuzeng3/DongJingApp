package com.example.dongjingapp.ui.training

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dongjingapp.data.repository.TrainingRepository
import kotlinx.coroutines.launch

class TrainingPlayerViewModel(
    application: Application,
    private val trainingRepository: TrainingRepository = TrainingRepository(application.applicationContext)
) : AndroidViewModel(application) {

    private var sessionStartMs: Long = 0L
    private var activeCourseId: String? = null

    fun beginSession(courseId: String, videoUrl: String) {
        sessionStartMs = System.currentTimeMillis()
        activeCourseId = courseId
    }

    fun endSession() {
        val start = sessionStartMs
        val courseId = activeCourseId
        if (start == 0L || courseId == null) return
        val durationSec = ((System.currentTimeMillis() - start) / 1000L).coerceAtLeast(0L)
        viewModelScope.launch {
            trainingRepository.recordSession(
                courseId = courseId,
                durationSeconds = durationSec
            )
        }
        sessionStartMs = 0L
        activeCourseId = null
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TrainingPlayerViewModel(application) as T
                }
            }
    }
}
