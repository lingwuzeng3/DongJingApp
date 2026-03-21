package com.example.dongjingapp.ui.video

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * 视频对比屏幕
 * TODO: 实现视频分屏对比功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCompareScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("视频对比") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B82F6),
                    titleContentColor = Color.White
                )
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "视频对比功能开发中...")
            // TODO: 实现视频分屏布局
            // TODO: 实现两个视频的同步播放
            // TODO: 实现播放控制
            // TODO: 实现对比分析功能
        }
    }
}