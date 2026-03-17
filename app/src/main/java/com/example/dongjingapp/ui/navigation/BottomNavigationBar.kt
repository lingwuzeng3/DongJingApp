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

/**
 * 底部导航栏
 * 包含首页、课程、AR训练、数据、个人中心五个主要入口
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    BottomAppBar {
        bottomNavigationItems.forEach {item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
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
        icon = androidx.compose.material.icons.Icons.Default.Home
    ),
    BottomNavigationItem(
        route = "courses",
        title = "课程",
        icon = androidx.compose.material.icons.Icons.Default.Book
    ),
    BottomNavigationItem(
        route = "ar",
        title = "AR训练",
        icon = androidx.compose.material.icons.Icons.Default.VideoCamera
    ),
    BottomNavigationItem(
        route = "stats",
        title = "数据",
        icon = androidx.compose.material.icons.Icons.Default.TrendingUp
    ),
    BottomNavigationItem(
        route = "profile",
        title = "我的",
        icon = androidx.compose.material.icons.Icons.Default.Person
    )
)