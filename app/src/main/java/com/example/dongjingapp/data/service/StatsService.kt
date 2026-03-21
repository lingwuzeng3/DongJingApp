package com.example.dongjingapp.data.service

/**
 * 数据统计服务
 * 提供训练数据和身体数据的统计功能
 */
class StatsService {
    
    /**
     * 获取周训练数据
     */
    fun getWeeklyStats(): List<DailyStats> {
        return listOf(
            DailyStats("周一", 45, 320),
            DailyStats("周二", 60, 450),
            DailyStats("周三", 30, 210),
            DailyStats("周四", 75, 580),
            DailyStats("周五", 45, 320),
            DailyStats("周六", 90, 680),
            DailyStats("周日", 60, 450)
        )
    }
    
    /**
     * 获取月训练数据
     */
    fun getMonthlyStats(): List<WeeklyStats> {
        return listOf(
            WeeklyStats(1, 240, 1800),
            WeeklyStats(2, 300, 2200),
            WeeklyStats(3, 270, 1950),
            WeeklyStats(4, 330, 2450)
        )
    }
    
    /**
     * 获取身体数据
     */
    fun getBodyStats(): BodyStats {
        return BodyStats(
            currentWeight = 65.0,
            targetWeight = 60.0,
            bmi = 21.2,
            weightChange = -2.5
        )
    }
    
    /**
     * 获取目标进度
     */
    fun getGoalProgress(): GoalProgress {
        return GoalProgress(
            weightLoss = 60,
            trainingFrequency = 80,
            courseCompletion = 70
        )
    }
}

/**
 * 每日训练统计
 */
data class DailyStats(
    val day: String,
    val duration: Int, // 训练时长（分钟）
    val calories: Int // 消耗卡路里
)

/**
 * 每周训练统计
 */
data class WeeklyStats(
    val week: Int,
    val totalDuration: Int, // 总训练时长（分钟）
    val totalCalories: Int // 总消耗卡路里
)

/**
 * 身体数据统计
 */
data class BodyStats(
    val currentWeight: Double, // 当前体重（kg）
    val targetWeight: Double, // 目标体重（kg）
    val bmi: Double, // BMI指数
    val weightChange: Double // 体重变化（kg）
)

/**
 * 目标进度
 */
data class GoalProgress(
    val weightLoss: Int, // 减脂目标完成百分比
    val trainingFrequency: Int, // 训练频率完成百分比
    val courseCompletion: Int // 课程完成百分比
)