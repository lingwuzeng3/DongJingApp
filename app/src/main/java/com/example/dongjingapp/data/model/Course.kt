package com.example.dongjingapp.data.model

/**
 * 课程模型
 * 包含课程基本信息
 */
data class Course(
    val id: String,
    val title: String,
    val description: String,
    val category: String, // 课程分类：有氧、力量、瑜伽等
    val difficulty: String, // 难度：初级、中级、高级
    val duration: Int, // 课程时长（分钟）
    val coverImage: String, // 封面图片URL
    val videoUrl: String, // 课程视频URL
    val trainerName: String, // 教练名称
    val rating: Float, // 评分
    val reviewCount: Int, // 评论数量
    val createdAt: Long // 创建时间戳
)