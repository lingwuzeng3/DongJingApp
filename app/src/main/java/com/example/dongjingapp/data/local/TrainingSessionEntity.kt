package com.example.dongjingapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_sessions")
data class TrainingSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val courseId: String,
    val startedAt: Long,
    val endedAt: Long,
    val durationSeconds: Long
)
