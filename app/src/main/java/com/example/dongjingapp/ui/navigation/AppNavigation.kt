package com.example.dongjingapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dongjingapp.ui.home.HomeScreen
import com.example.dongjingapp.ui.courses.CourseListScreen
import com.example.dongjingapp.ui.courses.CourseDetailScreen
import com.example.dongjingapp.ui.profile.ProfileScreen
import com.example.dongjingapp.ui.ar.ARScreen
import com.example.dongjingapp.ui.stats.StatsScreen
import com.example.dongjingapp.ui.video.VideoLibraryScreen
import com.example.dongjingapp.ui.video.VideoUploadScreen
import com.example.dongjingapp.ui.video.VideoCompareScreen

/**
 * 应用导航组件
 * 管理不同功能模块的页面切换
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                // 首页
                composable(route = "home") {
                    HomeScreen(navController = navController)
                }
                
                // 课程列表
                composable(route = "courses") {
                    CourseListScreen(navController = navController)
                }
                
                // 课程详情
                composable(route = "course/{courseId}") {
                    val courseId = it.arguments?.getString("courseId") ?: ""
                    CourseDetailScreen(
                        courseId = courseId,
                        onBack = { navController.popBackStack() }
                    )
                }
                
                // 个人中心
                composable(route = "profile") {
                    ProfileScreen()
                }
                
                // AR训练
                composable(route = "ar") {
                    ARScreen()
                }
                
                // 数据统计
                composable(route = "stats") {
                    StatsScreen()
                }
                
                // 视频管理
                composable(route = "video/library") {
                    VideoLibraryScreen()
                }
                
                // 视频上传
                composable(route = "video/upload") {
                    VideoUploadScreen()
                }
                
                // 视频对比
                composable(route = "video/compare") {
                    VideoCompareScreen()
                }
            }
        }
    }
}
