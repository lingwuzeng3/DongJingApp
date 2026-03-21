package com.example.dongjingapp.ui.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dongjingapp.data.service.VideoService

/**
 * 视频库屏幕
 * TODO: 实现视频列表展示和播放功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoLibraryScreen() {
    val videoService = VideoService()
    val videos = videoService.getVideos()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("视频库") },
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
        ) {
            // 视频列表
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(videos) {
                    VideoCard(video = it)
                }
            }
        }
    }
}

/**
 * 视频卡片
 */
@Composable
fun VideoCard(video: com.example.dongjingapp.data.service.Video) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 视频缩略图
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFE5E7EB))
            ) {
                // TODO: 实现视频缩略图显示
                Text(
                    text = "视频缩略图",
                    color = Color(0xFF6B7280),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            // 视频信息
            Text(
                text = video.title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )
            Text(
                text = "${video.duration}秒 · ${video.views}次观看",
                fontSize = 12.sp,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}