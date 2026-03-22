package com.example.dongjingapp.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Person

/**
 * 底部导航栏
 * 包含首页、课程、AR训练、数据、个人中心五个主要入口
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    BottomAppBar {
        bottomNavigationItems.forEach { item ->
            val route = currentDestination?.route.orEmpty()
            val selected = when (item.route) {
                "courses" -> route.startsWith("courses")
                else -> currentDestination?.hierarchy?.any { it.route == item.route } == true
            }
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selected,
                onClick = {
                    if (item.route == "home") {
                        navController.navigate("home") {
                            // 弹出栈上 home 及其之上的所有页面（含从首页跳进课程列表/详情的记录），再进入干净的首页
                            popUpTo("home") { inclusive = true }
                            launchSingleTop = true
                            restoreState = false
                        }
                        return@NavigationBarItem
                    }
                    val targetRoute = if (item.route == "courses") {
                        "courses?initialCategory=全部"
                    } else {
                        item.route
                    }
                    navController.navigate(targetRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF3B82F6), // 主色调：活力蓝
                    selectedTextColor = Color(0xFF3B82F6),
                    unselectedIconColor = Color(0xFF6B7280), // 中性色：中灰
                    unselectedTextColor = Color(0xFF6B7280),
                )
            )
        }
    }
}

/**
 * 底部导航项
 */
data class BottomNavigationItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

/**
 * 底部导航项列表
 */
val bottomNavigationItems = listOf(
    BottomNavigationItem(
        route = "home",
        title = "首页",
        icon = Icons.Filled.Home
    ),
    BottomNavigationItem(
        route = "courses",
        title = "课程",
        icon = Icons.Filled.Book
    ),
    BottomNavigationItem(
        route = "ar",
        title = "AR训练",
        icon = Icons.Filled.Videocam
    ),
    BottomNavigationItem(
        route = "stats",
        title = "数据",
        icon = Icons.AutoMirrored.Filled.TrendingUp
    ),
    BottomNavigationItem(
        route = "profile",
        title = "我的",
        icon = Icons.Filled.Person
    )
)