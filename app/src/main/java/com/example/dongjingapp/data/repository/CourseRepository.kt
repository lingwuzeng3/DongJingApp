package com.example.dongjingapp.data.repository

import com.example.dongjingapp.data.model.Course
import com.example.dongjingapp.data.service.CourseService

/**
 * 课程数据仓库（封装 [CourseService]，便于后续替换为网络/数据库实现）
 */
class CourseRepository(
    private val courseService: CourseService = CourseService()
) {
    fun getCourses(): List<Course> = courseService.getCourses()
    fun getCourseById(id: String): Course? = courseService.getCourseById(id)
}
