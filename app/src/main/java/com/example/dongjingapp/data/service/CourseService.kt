package com.example.dongjingapp.data.service

import com.example.dongjingapp.data.DemoMedia
import com.example.dongjingapp.data.model.Course

/**
 * 课程服务
 * 提供课程相关的数据操作
 */
class CourseService {
    
    /**
     * 获取课程列表
     */
    fun getCourses(): List<Course> {
        return courseList
    }
    
    /**
     * 根据分类筛选课程
     */
    fun getCoursesByCategory(category: String): List<Course> {
        return courseList.filter { it.category == category }
    }
    
    /**
     * 根据难度筛选课程
     */
    fun getCoursesByDifficulty(difficulty: String): List<Course> {
        return courseList.filter { it.difficulty == difficulty }
    }
    
    /**
     * 根据ID获取课程详情
     */
    fun getCourseById(id: String): Course? {
        return courseList.find { it.id == id }
    }
    
    /**
     * 模拟课程数据
     */
    private val courseList = listOf(
        Course(
            id = "1",
            title = "晨间瑜伽拉伸",
            description = "适合早晨起床后的拉伸练习，帮助唤醒身体，提高柔韧性",
            category = "瑜伽",
            difficulty = "初级",
            duration = 20,
            coverImage = "https://neeko-copilot.bytedance.net/api/text2image?prompt=yoga%20stretching%20in%20morning%20light&size=800x600",
            videoUrl = DemoMedia.SAMPLE_MP4_SHORT,
            trainerName = "张教练",
            rating = 4.8f,
            reviewCount = 128,
            createdAt = System.currentTimeMillis()
        ),
        Course(
            id = "2",
            title = "高强度间歇训练",
            description = "短时间内提高心率，燃烧脂肪，适合想要快速减肥的人群",
            category = "有氧",
            difficulty = "高级",
            duration = 30,
            coverImage = "https://neeko-copilot.bytedance.net/api/text2image?prompt=high%20intensity%20interval%20training%20workout&size=800x600",
            videoUrl = DemoMedia.SAMPLE_MP4_ALT,
            trainerName = "李教练",
            rating = 4.7f,
            reviewCount = 95,
            createdAt = System.currentTimeMillis()
        ),
        Course(
            id = "3",
            title = "力量训练基础",
            description = "针对初学者的力量训练，学习正确的动作姿势和发力方式",
            category = "力量",
            difficulty = "初级",
            duration = 45,
            coverImage = "https://neeko-copilot.bytedance.net/api/text2image?prompt=strength%20training%20basics%20gym&size=800x600",
            videoUrl = DemoMedia.SAMPLE_MP4_SHORT,
            trainerName = "王教练",
            rating = 4.9f,
            reviewCount = 156,
            createdAt = System.currentTimeMillis()
        ),
        Course(
            id = "4",
            title = "冥想放松练习",
            description = "通过冥想和呼吸练习，缓解压力，提高专注力",
            category = "瑜伽",
            difficulty = "初级",
            duration = 15,
            coverImage = "https://neeko-copilot.bytedance.net/api/text2image?prompt=meditation%20relaxation%20peaceful%20setting&size=800x600",
            videoUrl = DemoMedia.SAMPLE_MP4_SHORT,
            trainerName = "刘教练",
            rating = 4.6f,
            reviewCount = 87,
            createdAt = System.currentTimeMillis()
        ),
        Course(
            id = "5",
            title = "核心训练进阶",
            description = "加强核心肌群的训练，提高身体稳定性和平衡能力",
            category = "力量",
            difficulty = "中级",
            duration = 35,
            coverImage = "https://neeko-copilot.bytedance.net/api/text2image?prompt=core%20training%20advanced%20exercises&size=800x600",
            videoUrl = DemoMedia.SAMPLE_MP4_SHORT,
            trainerName = "陈教练",
            rating = 4.8f,
            reviewCount = 112,
            createdAt = System.currentTimeMillis()
        ),
        Course(
            id = "6",
            title = "舞蹈燃脂课程",
            description = "结合舞蹈元素的有氧运动，在欢乐中燃烧脂肪",
            category = "有氧",
            difficulty = "中级",
            duration = 40,
            coverImage = "https://neeko-copilot.bytedance.net/api/text2image?prompt=dance%20fitness%20class%20energetic&size=800x600",
            videoUrl = DemoMedia.SAMPLE_MP4_ALT,
            trainerName = "赵教练",
            rating = 4.9f,
            reviewCount = 143,
            createdAt = System.currentTimeMillis()
        ),
        Course(
            id = "7",
            title = "全身动态拉伸",
            description = "训练前后全身关节与肌肉拉伸，降低受伤风险",
            category = "拉伸",
            difficulty = "初级",
            duration = 12,
            coverImage = "https://neeko-copilot.bytedance.net/api/text2image?prompt=stretching%20exercise%20flexibility&size=800x600",
            videoUrl = DemoMedia.SAMPLE_MP4_SHORT,
            trainerName = "周教练",
            rating = 4.7f,
            reviewCount = 76,
            createdAt = System.currentTimeMillis()
        ),
        Course(
            id = "8",
            title = "办公族肩颈拉伸",
            description = "针对久坐人群的肩颈与上背部放松",
            category = "拉伸",
            difficulty = "初级",
            duration = 10,
            coverImage = "https://neeko-copilot.bytedance.net/api/text2image?prompt=office%20neck%20stretch%20relax&size=800x600",
            videoUrl = DemoMedia.SAMPLE_MP4_ALT,
            trainerName = "周教练",
            rating = 4.5f,
            reviewCount = 54,
            createdAt = System.currentTimeMillis()
        )
    )
}