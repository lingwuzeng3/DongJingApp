package com.example.dongjingapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dongjingapp.ui.ar.ARScreen
import com.example.dongjingapp.ui.courses.CourseDetailScreen
import com.example.dongjingapp.ui.courses.CourseListScreen
import com.example.dongjingapp.ui.home.HomeScreen
import com.example.dongjingapp.ui.profile.ProfileScreen
import com.example.dongjingapp.ui.settings.AccountSettingsScreen
import com.example.dongjingapp.ui.settings.GeneralSettingsScreen
import com.example.dongjingapp.ui.settings.NotificationSettingsScreen
import com.example.dongjingapp.ui.settings.PrivacySettingsScreen
import com.example.dongjingapp.ui.stats.StatsScreen
import com.example.dongjingapp.ui.training.TrainingPlayerScreen
import com.example.dongjingapp.ui.video.VideoCompareScreen
import com.example.dongjingapp.ui.video.VideoLibraryScreen
import com.example.dongjingapp.ui.video.VideoPlayerScreen
import com.example.dongjingapp.ui.video.VideoUploadScreen

/**
 * 应用导航组件
 * 管理不同功能模块的页面切换
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val route = navBackStackEntry?.destination?.route.orEmpty()
    val hideBottomBar =
        route.startsWith("training/") ||
            route.startsWith("video/player/") ||
            route == "video/upload" ||
            route.startsWith("profile/settings/")

    Scaffold(
        bottomBar = {
            if (!hideBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable(route = "home") {
                    HomeScreen(navController = navController)
                }

                composable(
                    route = "courses?initialCategory={initialCategory}",
                    arguments = listOf(
                        navArgument("initialCategory") {
                            type = NavType.StringType
                            defaultValue = "全部"
                        }
                    )
                ) { backStackEntry ->
                    val initialCategory =
                        backStackEntry.arguments?.getString("initialCategory") ?: "全部"
                    CourseListScreen(
                        navController = navController,
                        initialCategory = initialCategory
                    )
                }

                composable(route = "course/{courseId}") {
                    val courseId = it.arguments?.getString("courseId") ?: ""
                    CourseDetailScreen(
                        courseId = courseId,
                        onBack = { navController.popBackStack() },
                        onStartTraining = {
                            navController.navigate("training/$courseId")
                        }
                    )
                }

                composable(route = "training/{courseId}") {
                    val courseId = it.arguments?.getString("courseId") ?: ""
                    TrainingPlayerScreen(
                        courseId = courseId,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(route = "profile") {
                    ProfileScreen(
                        onAccountClick = { navController.navigate("profile/settings/account") },
                        onNotificationClick = { navController.navigate("profile/settings/notification") },
                        onPrivacyClick = { navController.navigate("profile/settings/privacy") },
                        onGeneralClick = { navController.navigate("profile/settings/general") }
                    )
                }

                composable(route = "profile/settings/account") {
                    AccountSettingsScreen(onBack = { navController.popBackStack() })
                }

                composable(route = "profile/settings/notification") {
                    NotificationSettingsScreen(onBack = { navController.popBackStack() })
                }

                composable(route = "profile/settings/privacy") {
                    PrivacySettingsScreen(onBack = { navController.popBackStack() })
                }

                composable(route = "profile/settings/general") {
                    GeneralSettingsScreen(onBack = { navController.popBackStack() })
                }

                composable(route = "ar") {
                    ARScreen()
                }

                composable(route = "stats") {
                    StatsScreen()
                }

                composable(route = "video/library") {
                    VideoLibraryScreen(navController = navController)
                }

                composable(
                    route = "video/player/{videoId}",
                    arguments = listOf(navArgument("videoId") { type = NavType.StringType })
                ) { entry ->
                    val videoId = entry.arguments?.getString("videoId").orEmpty()
                    VideoPlayerScreen(
                        videoId = videoId,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(route = "video/upload") {
                    VideoUploadScreen()
                }

                composable(route = "video/compare") {
                    VideoCompareScreen()
                }
            }
        }
    }
}
