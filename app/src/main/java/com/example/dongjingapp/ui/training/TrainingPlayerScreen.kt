package com.example.dongjingapp.ui.training

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.app.Application
import com.example.dongjingapp.data.DemoMedia
import com.example.dongjingapp.data.repository.CourseRepository
import com.example.dongjingapp.data.repository.SettingsRepository
import com.example.dongjingapp.util.isWifiConnected
import com.example.dongjingapp.ui.media.ExoPlayerVideoView
import com.example.dongjingapp.ui.media.rememberExoPlayer
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingPlayerScreen(
    courseId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val settingsRepository = SettingsRepository(context.applicationContext as Application)
    val settings by settingsRepository.settingsFlow.collectAsStateWithLifecycle(initialValue = com.example.dongjingapp.data.settings.AppSettings())
    val viewModel: TrainingPlayerViewModel = viewModel(
        factory = TrainingPlayerViewModel.factory(context.applicationContext as Application)
    )
    val courseRepository = CourseRepository()
    val course = courseRepository.getCourseById(courseId)
    val title = course?.title ?: "训练播放"
    val uri = course?.videoUrl?.takeIf { it.isNotBlank() } ?: DemoMedia.SAMPLE_MP4_SHORT

    val shouldAutoPlay = settings.autoPlayVideo &&
        (!settings.wifiOnlyAutoPlay || isWifiConnected(context))
    val player = rememberExoPlayer(videoUri = uri, playWhenReady = shouldAutoPlay)

    androidx.compose.runtime.DisposableEffect(courseId, uri) {
        viewModel.beginSession(courseId = courseId, videoUrl = uri)
        onDispose {
            viewModel.endSession()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B82F6),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = if (course != null) "${course.category} · ${course.duration} 分钟" else "演示视频",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            ExoPlayerVideoView(
                player = player,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}
