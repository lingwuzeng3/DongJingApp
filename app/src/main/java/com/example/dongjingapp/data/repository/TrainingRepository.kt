package com.example.dongjingapp.data.repository

import android.content.Context
import com.example.dongjingapp.data.local.DongJingDatabase
import com.example.dongjingapp.data.local.TrainingSessionEntity
import kotlinx.coroutines.flow.Flow

class TrainingRepository(context: Context) {

    private val dao = DongJingDatabase.getInstance(context).trainingSessionDao()

    suspend fun recordSession(courseId: String, durationSeconds: Long) {
        val now = System.currentTimeMillis()
        dao.insert(
            TrainingSessionEntity(
                courseId = courseId,
                startedAt = now - durationSeconds * 1000L,
                endedAt = now,
                durationSeconds = durationSeconds
            )
        )
    }

    fun observeRecentSessions(limit: Int = 50): Flow<List<TrainingSessionEntity>> =
        dao.observeRecent(limit)

    suspend fun totalDurationSecondsSince(sinceMs: Long): Long =
        dao.totalDurationSecondsSince(sinceMs)
}
