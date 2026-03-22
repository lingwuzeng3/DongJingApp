package com.example.dongjingapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingSessionDao {
    @Insert
    suspend fun insert(session: TrainingSessionEntity): Long

    @Query("SELECT * FROM training_sessions ORDER BY endedAt DESC LIMIT :limit")
    fun observeRecent(limit: Int = 50): Flow<List<TrainingSessionEntity>>

    @Query("SELECT COALESCE(SUM(durationSeconds), 0) FROM training_sessions WHERE endedAt >= :sinceMs")
    suspend fun totalDurationSecondsSince(sinceMs: Long): Long
}
