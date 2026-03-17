package com.example.dongjingapp.data.model

/**
 * 用户模型
 * 包含用户基本信息
 */
data class User(
    val id: String,
    val username: String,
    val email: String,
    val avatar: String? = null,
    val height: Float? = null, // 身高（cm）
    val weight: Float? = null, // 体重（kg）
    val fitnessGoal: String? = null, // 健身目标
    val createdAt: Long // 创建时间戳
)