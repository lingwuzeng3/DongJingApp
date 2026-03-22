package com.example.dongjingapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TrainingSessionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DongJingDatabase : RoomDatabase() {
    abstract fun trainingSessionDao(): TrainingSessionDao

    companion object {
        @Volatile
        private var instance: DongJingDatabase? = null

        fun getInstance(context: Context): DongJingDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DongJingDatabase::class.java,
                    "dongjing.db"
                ).build().also { instance = it }
            }
        }
    }
}
