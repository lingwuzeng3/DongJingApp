package com.example.dongjingapp.data.service

import com.example.dongjingapp.data.model.User

/**
 * 用户服务
 * 提供用户相关的数据操作
 */
class UserService {
    
    /**
     * 获取当前用户信息
     */
    fun getCurrentUser(): User {
        return currentUser
    }
    
    /**
     * 更新用户信息
     */
    fun updateUser(user: User): User {
        // 这里可以实现用户信息更新逻辑
        return user
    }
    
    /**
     * 获取用户训练统计数据
     */
    fun getUserStats(): UserStats {
        return userStats
    }
    
    /**
     * 获取用户成就
     */
    fun getUserAchievements(): List<Achievement> {
        return userAchievements
    }
    
    /**
     * 当前用户信息
     */
    private val currentUser = User(
        id = "1",
        username = "运动达人",
        email = "user@example.com",
        avatar = "https://neeko-copilot.bytedance.net/api/text2image?prompt=fitness%20user%20avatar%20profile%20picture&size=200x200",
        height = 175f,
        weight = 65f,
        fitnessGoal = "减脂增肌",
        createdAt = System.currentTimeMillis()
    )
    
    /**
     * 用户训练统计数据
     */
    private val userStats = UserStats(
        completedCourses = 28,
        totalTrainingTime = 1245, // 分钟
        totalCaloriesBurned = 8760, // 卡路里
        streakDays = 7 // 连续训练天数
    )
    
    /**
     * 用户成就列表
     */
    private val userAchievements = listOf(
        Achievement(
            id = "1",
            title = "初露锋芒",
            description = "完成第一次训练",
            unlockedAt = System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000
        ),
        Achievement(
            id = "2",
            title = "坚持一周",
            description = "连续训练7天",
            unlockedAt = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000
        ),
        Achievement(
            id = "3",
            title = "课程达人",
            description = "完成20节课程",
            unlockedAt = System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000
        )
    )
}

/**
 * 用户训练统计数据
 */
data class UserStats(
    val completedCourses: Int, // 完成课程数
    val totalTrainingTime: Int, // 总训练时间（分钟）
    val totalCaloriesBurned: Int, // 总消耗卡路里
    val streakDays: Int // 连续训练天数
)

/**
 * 成就
 */
data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val unlockedAt: Long // 解锁时间戳
)