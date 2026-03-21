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
 * 视频上传屏幕
 * TODO: 实现视频选择、压缩和上传功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoUploadScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("视频上传") },
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
            Text(text = "视频上传功能开发中...")
            // TODO: 实现视频选择功能
            // TODO: 实现视频压缩功能
            // TODO: 实现视频上传功能
            // TODO: 显示上传进度
        }
    }
}