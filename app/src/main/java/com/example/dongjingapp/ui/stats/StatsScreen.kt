package com.example.dongjingapp.ui.stats

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dongjingapp.data.service.BodyStats
import com.example.dongjingapp.data.service.DailyStats
import com.example.dongjingapp.data.service.GoalProgress
import com.example.dongjingapp.data.service.StatsService
import com.example.dongjingapp.data.service.UserService
import com.example.dongjingapp.data.service.WeeklyStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen() {
    val context = LocalContext.current
    val viewModel: StatsViewModel = viewModel(
        factory = StatsViewModel.factory(context.applicationContext as Application)
    )
    val localWeekMinutes by viewModel.localWeekMinutes.collectAsStateWithLifecycle()

    val statsService = StatsService()
    val userService = UserService()
    val weeklyStats = statsService.getWeeklyStats()
    val monthlyStats = statsService.getMonthlyStats()
    val bodyStats = statsService.getBodyStats()
    val goalProgress = statsService.getGoalProgress()
    val userStats = userService.getUserStats()

    val pullState = rememberPullToRefreshState()
    val refreshing = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("数据统计") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B82F6),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = refreshing.value,
            onRefresh = {
                refreshing.value = true
                scope.launch {
                    viewModel.refreshLocalWeek()
                    delay(600)
                    refreshing.value = false
                }
            },
            state = pullState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                StatsOverviewCard(
                    weeklyStats = weeklyStats,
                    localWeekMinutes = localWeekMinutes,
                    userStats = userStats
                )
                WeeklyStatsCard(stats = weeklyStats)
                MonthlyStatsCard(stats = monthlyStats)
                BodyStatsCard(stats = bodyStats)
                GoalProgressCard(progress = goalProgress)
                Column(modifier = Modifier.height(32.dp)) {}
            }
        }
    }
}

@Composable
fun StatsOverviewCard(
    weeklyStats: List<DailyStats>,
    localWeekMinutes: Int,
    userStats: com.example.dongjingapp.data.service.UserStats
) {
    val demoWeekMinutes = weeklyStats.sumOf { it.duration }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "训练概览",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Text(
                text = "本周（演示数据）训练时长: ${demoWeekMinutes} 分钟",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "本周（本地记录）训练时长: $localWeekMinutes 分钟",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF3B82F6),
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "总消耗卡路里（个人）: ${userStats.totalCaloriesBurned}",
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(text = "完成课程数: ${userStats.completedCourses}")
            Text(text = "连续训练: ${userStats.streakDays} 天")
        }
    }
}

@Composable
fun WeeklyStatsCard(stats: List<DailyStats>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "本周训练",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            SimpleBarChart(
                values = stats.map { it.duration.toFloat() },
                labels = stats.map { it.day }
            )
            stats.forEach {
                Text(
                    text = "${it.day}: ${it.duration} 分钟 · ${it.calories} 卡",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun MonthlyStatsCard(stats: List<WeeklyStats>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "本月训练",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            SimpleLineChart(
                values = stats.map { it.totalDuration.toFloat() },
                labels = stats.map { "第${it.week}周" }
            )
            stats.forEach {
                Text(
                    text = "第 ${it.week} 周: ${it.totalDuration} 分钟 · ${it.totalCalories} 卡",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun BodyStatsCard(stats: BodyStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "身体数据",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            val current = stats.currentWeight.toFloat()
            val target = stats.targetWeight.toFloat()
            val maxW = kotlin.math.max(current, target) * 1.1f
            SimpleBarChart(
                values = listOf(current, target),
                labels = listOf("当前", "目标"),
                barColor = Color(0xFF8B5CF6)
            )
            Text(
                text = "当前体重: ${stats.currentWeight} kg",
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(text = "目标体重: ${stats.targetWeight} kg")
            Text(text = "BMI: ${stats.bmi}")
            Text(text = "体重变化: ${stats.weightChange} kg")
        }
    }
}

@Composable
fun GoalProgressCard(progress: GoalProgress) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "目标进度",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            GoalProgressRow("减脂目标", progress.weightLoss)
            GoalProgressRow("训练频率", progress.trainingFrequency)
            GoalProgressRow("课程完成", progress.courseCompletion)
        }
    }
}

@Composable
private fun GoalProgressRow(label: String, percent: Int) {
    val p = (percent.coerceIn(0, 100)) / 100f
    Text(text = label, style = MaterialTheme.typography.bodySmall)
    LinearProgressIndicator(
        progress = { p },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = Color(0xFF3B82F6),
        trackColor = Color(0xFFE5E7EB)
    )
    Text(
        text = "$percent%",
        style = MaterialTheme.typography.labelSmall,
        color = Color(0xFF6B7280),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}
