package com.example.dongjingapp.ui.stats

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.dongjingapp.data.service.StatsService

/**
 * 数据统计屏幕
 * TODO: 实现图表展示和数据可视化功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen() {
    val statsService = StatsService()
    val weeklyStats = statsService.getWeeklyStats()
    val monthlyStats = statsService.getMonthlyStats()
    val bodyStats = statsService.getBodyStats()
    
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {
            // 训练概览卡片
            StatsOverviewCard()
            
            // 周训练数据卡片
            WeeklyStatsCard(stats = weeklyStats)
            
            // 月训练数据卡片
            MonthlyStatsCard(stats = monthlyStats)
            
            // 身体数据卡片
            BodyStatsCard(stats = bodyStats)
            
            // 目标进度卡片
            GoalProgressCard()
            
            // 底部间距
            Column(modifier = Modifier.height(32.dp)) {}
        }
    }
}

/**
 * 训练概览卡片
 */
@Composable
fun StatsOverviewCard() {
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
            
            // TODO: 实现训练概览数据展示
            Text(text = "总训练时长: 20小时30分钟")
            Text(text = "总消耗卡路里: 8760")
            Text(text = "完成课程数: 28")
            Text(text = "连续训练: 7天")
        }
    }
}

/**
 * 周训练数据卡片
 */
@Composable
fun WeeklyStatsCard(stats: List<com.example.dongjingapp.data.service.DailyStats>) {
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
            
            // TODO: 实现周训练数据图表
            stats.forEach {
                Text(text = "${it.day}: ${it.duration}分钟, ${it.calories}卡路里")
            }
        }
    }
}

/**
 * 月训练数据卡片
 */
@Composable
fun MonthlyStatsCard(stats: List<com.example.dongjingapp.data.service.WeeklyStats>) {
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
            
            // TODO: 实现月训练数据图表
            stats.forEach {
                Text(text = "第${it.week}周: ${it.totalDuration}分钟, ${it.totalCalories}卡路里")
            }
        }
    }
}

/**
 * 身体数据卡片
 */
@Composable
fun BodyStatsCard(stats: com.example.dongjingapp.data.service.BodyStats) {
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
            
            // TODO: 实现身体数据变化图表
            Text(text = "当前体重: ${stats.currentWeight}kg")
            Text(text = "目标体重: ${stats.targetWeight}kg")
            Text(text = "BMI: ${stats.bmi}")
            Text(text = "体重变化: ${stats.weightChange}kg")
        }
    }
}

/**
 * 目标进度卡片
 */
@Composable
fun GoalProgressCard() {
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
            
            // TODO: 实现目标进度条
            Text(text = "减脂目标: 已完成 60%")
            Text(text = "训练频率: 已完成 80%")
            Text(text = "课程完成: 已完成 70%")
        }
    }
}