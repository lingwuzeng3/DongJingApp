package com.example.dongjingapp.data.service

/**
 * 视频服务
 * 提供视频管理相关的功能
 */
class VideoService {
    
    /**
     * 获取视频列表
     */
    fun getVideos(): List<Video> {
        return videoList
    }
    
    /**
     * 上传视频
     */
    fun uploadVideo(video: Video): Boolean {
        // TODO: 实现视频上传逻辑
        return true
    }
    
    /**
     * 删除视频
     */
    fun deleteVideo(videoId: String): Boolean {
        // TODO: 实现视频删除逻辑
        return true
    }
    
    /**
     * 获取视频详情
     */
    fun getVideoById(videoId: String): Video? {
        return videoList.find { it.id == videoId }
    }
    
    /**
     * 视频列表
     */
    private val videoList = listOf(
        Video(
            id = "1",
            title = "晨间瑜伽练习",
            description = "适合早晨起床后的瑜伽练习，帮助唤醒身体",
            duration = 600, // 10分钟
            views = 1234,
            thumbnailUrl = "",
            videoUrl = "",
            createdAt = System.currentTimeMillis()
        ),
        Video(
            id = "2",
            title = "高强度间歇训练",
            description = "15分钟高强度间歇训练，快速燃烧脂肪",
            duration = 900, // 15分钟
            views = 2345,
            thumbnailUrl = "",
            videoUrl = "",
            createdAt = System.currentTimeMillis()
        ),
        Video(
            id = "3",
            title = "核心力量训练",
            description = "针对核心肌群的力量训练，提高身体稳定性",
            duration = 1200, // 20分钟
            views = 1876,
            thumbnailUrl = "",
            videoUrl = "",
            createdAt = System.currentTimeMillis()
        ),
        Video(
            id = "4",
            title = "冥想放松练习",
            description = "10分钟冥想练习，缓解压力，提高专注力",
            duration = 600, // 10分钟
            views = 987,
            thumbnailUrl = "",
            videoUrl = "",
            createdAt = System.currentTimeMillis()
        )
    )
}

/**
 * 视频模型
 */
data class Video(
    val id: String,
    val title: String,
    val description: String,
    val duration: Int, // 视频时长（秒）
    val views: Int, // 观看次数
    val thumbnailUrl: String, // 缩略图URL
    val videoUrl: String, // 视频URL
    val createdAt: Long // 创建时间戳
)