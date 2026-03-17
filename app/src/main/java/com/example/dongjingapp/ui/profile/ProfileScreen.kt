package com.example.dongjingapp.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeRefresh
import androidx.compose.material3.SwipeRefreshState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dongjingapp.data.service.UserService

/**
 * 个人中心屏幕
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val userService = UserService()
    val user = userService.getCurrentUser()
    val userStats = userService.getUserStats()
    
    // 状态管理
    val (isRefreshing, setIsRefreshing) = remember {
        mutableStateOf(false)
    }
    val (isLoading, setIsLoading) = remember {
        mutableStateOf(false)
    }
    
    // 刷新功能
    val onRefresh = {
        setIsRefreshing(true)
        // 模拟网络请求延迟
        kotlinx.coroutines.MainScope().launch {
            kotlinx.coroutines.delay(1000)
            setIsRefreshing(false)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("个人中心") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B82F6),
                    titleContentColor = Color.White
                )
            )
        }
    ) {
        SwipeRefresh(
            state = SwipeRefreshState(isRefreshing),
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                // 顶部用户信息区域
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + scaleIn()
                ) {
                    UserInfoSection(user = user)
                }
                
                // 身体数据卡片
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + scaleIn(),
                    initialVisibility = false
                ) {
                    BodyDataSection(user = user)
                }
                
                // 训练统计卡片
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + scaleIn(),
                    initialVisibility = false
                ) {
                    TrainingStatsSection(stats = userStats)
                }
                
                // 设置功能模块
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + scaleIn(),
                    initialVisibility = false
                ) {
                    SettingsSection()
                }
                
                // 其他功能
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + scaleIn(),
                    initialVisibility = false
                ) {
                    OtherSection()
                }
                
                // 底部间距
                Box(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/**
 * 用户信息区域
 */
@Composable
fun UserInfoSection(user: com.example.dongjingapp.data.model.User) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color(0xFF3B82F6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 头像
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.avatar)
                    .crossfade(true)
                    .build(),
                contentDescription = user.username,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            
            // 用户名
            Text(
                text = user.username,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )
            
            // 邮箱
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8),
                modifier = Modifier.padding(top = 4.dp)
            )
            
            // 编辑资料按钮
            androidx.compose.material3.TextButton(
                onClick = {},
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(Color.White.copy(alpha = 0.2), MaterialTheme.shapes.medium)
            ) {
                Text(
                    text = "编辑资料",
                    color = Color.White
                )
            }
        }
    }
}

/**
 * 身体数据卡片
 */
@Composable
fun BodyDataSection(user: com.example.dongjingapp.data.model.User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "身体数据",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // 身高
                DataItem(
                    label = "身高",
                    value = "${user.height ?: "--"} cm"
                )
                
                // 体重
                DataItem(
                    label = "体重",
                    value = "${user.weight ?: "--"} kg"
                )
                
                // BMI
                DataItem(
                    label = "BMI",
                    value = calculateBMI(user.height, user.weight)
                )
            }
            
            // 健身目标
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "健身目标",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Text(
                    text = user.fitnessGoal ?: "未设置",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}

/**
 * 计算BMI指数
 */
fun calculateBMI(height: Float?, weight: Float?): String {
    if (height == null || weight == null || height == 0f) {
        return "--"
    }
    val heightInMeters = height / 100
    val bmi = weight / (heightInMeters * heightInMeters)
    return String.format("%.1f", bmi)
}

/**
 * 数据项
 */
@Composable
fun DataItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3B82F6)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

/**
 * 训练统计卡片
 */
@Composable
fun TrainingStatsSection(stats: com.example.dongjingapp.data.service.UserStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "训练统计",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // 完成课程数
                StatItem(
                    icon = androidx.compose.material.icons.Icons.Default.Book,
                    value = stats.completedCourses.toString(),
                    label = "课程"
                )
                
                // 总训练时间
                StatItem(
                    icon = androidx.compose.material.icons.Icons.Default.AccessTime,
                    value = "${stats.totalTrainingTime / 60}h",
                    label = "时长"
                )
                
                // 消耗卡路里
                StatItem(
                    icon = androidx.compose.material.icons.Icons.Default.LocalFireDepartment,
                    value = stats.totalCaloriesBurned.toString(),
                    label = "卡路里"
                )
                
                // 连续训练天数
                StatItem(
                    icon = androidx.compose.material.icons.Icons.Default.Fireplace,
                    value = stats.streakDays.toString(),
                    label = "连续"
                )
            }
        }
    }
}

/**
 * 统计项
 */
@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF3B82F6),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF6B7280),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

/**
 * 设置功能模块
 */
@Composable
fun SettingsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            SettingItem(
                icon = androidx.compose.material.icons.Icons.Default.AccountCircle,
                title = "账号设置",
                onClick = {}
            )
            SettingItem(
                icon = androidx.compose.material.icons.Icons.Default.Notifications,
                title = "通知设置",
                onClick = {}
            )
            SettingItem(
                icon = androidx.compose.material.icons.Icons.Default.Lock,
                title = "隐私设置",
                onClick = {}
            )
            SettingItem(
                icon = androidx.compose.material.icons.Icons.Default.Settings,
                title = "通用设置",
                onClick = {}
            )
        }
    }
}

/**
 * 其他功能模块
 */
@Composable
fun OtherSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            SettingItem(
                icon = androidx.compose.material.icons.Icons.Default.Info,
                title = "关于我们",
                onClick = {}
            )
            SettingItem(
                icon = androidx.compose.material.icons.Icons.Default.Help,
                title = "帮助与反馈",
                onClick = {}
            )
            SettingItem(
                icon = androidx.compose.material.icons.Icons.Default.Logout,
                title = "退出登录",
                onClick = {},
                isLast = true
            )
        }
    }
}

/**
 * 设置项
 */
@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit,
    isLast: Boolean = false
) {
    androidx.compose.material3.ListItem(
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF6B7280)
            )
        },
        headlineContent = {
            Text(text = title)
        },
        trailingContent = {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.ChevronRight,
                contentDescription = "更多",
                tint = Color(0xFF9CA3AF)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .run {
                if (!isLast) {
                    this.padding(bottom = 1.dp)
                } else {
                    this
                }
            }
            .background(if (!isLast) Color(0xFFF3F4F6) else Color.Transparent),
        onClick = onClick
    )
}