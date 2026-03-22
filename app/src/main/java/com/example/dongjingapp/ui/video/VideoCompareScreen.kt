package com.example.dongjingapp.ui.video

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dongjingapp.data.service.VideoService
import com.example.dongjingapp.ui.media.ExoPlayerVideoView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * 双视频分屏对比：统一进度比例（0~1）同步拖动与播放/暂停
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoCompareScreen() {
    val videos = VideoService().getVideos()
    val left = videos.getOrNull(0)
    val right = videos.getOrNull(1)
    val leftUri = left?.videoUrl.orEmpty()
    val rightUri = right?.videoUrl.orEmpty()
    val d1 = (left?.duration ?: 1).toLong() * 1000L
    val d2 = (right?.duration ?: 1).toLong() * 1000L

    val context = androidx.compose.ui.platform.LocalContext.current
    val player1 = remember(leftUri) {
        ExoPlayer.Builder(context).build().apply {
            if (leftUri.isNotBlank()) {
                setMediaItem(MediaItem.fromUri(leftUri))
                prepare()
            }
        }
    }
    val player2 = remember(rightUri) {
        ExoPlayer.Builder(context).build().apply {
            if (rightUri.isNotBlank()) {
                setMediaItem(MediaItem.fromUri(rightUri))
                prepare()
            }
        }
    }

    DisposableEffect(player1, player2) {
        onDispose {
            player1.release()
            player2.release()
        }
    }

    var playing by remember { mutableStateOf(false) }
    var positionFraction by remember { mutableFloatStateOf(0f) }

    fun seekBoth(fraction: Float) {
        positionFraction = fraction.coerceIn(0f, 1f)
        player1.seekTo((d1 * positionFraction).toLong().coerceAtMost(d1))
        player2.seekTo((d2 * positionFraction).toLong().coerceAtMost(d2))
    }

    LaunchedEffect(playing) {
        while (playing && isActive) {
            delay(250)
            val p1 = player1.currentPosition.coerceIn(0L, d1)
            positionFraction = (p1.toFloat() / d1.toFloat()).coerceIn(0f, 1f)
        }
    }

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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "上：${left?.title ?: "—"}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(8.dp)
            )
            ExoPlayerVideoView(
                player = player1,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "下：${right?.title ?: "—"}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(8.dp)
            )
            ExoPlayerVideoView(
                player = player2,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                IconButton(onClick = {
                    playing = !playing
                    player1.playWhenReady = playing
                    player2.playWhenReady = playing
                    if (playing) {
                        player1.play()
                        player2.play()
                    } else {
                        player1.pause()
                        player2.pause()
                    }
                }) {
                    Icon(
                        imageVector = if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (playing) "暂停" else "播放"
                    )
                }
                Text(
                    text = "按左侧视频进度同步",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Slider(
                value = positionFraction,
                onValueChange = { f ->
                    seekBoth(f)
                },
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "时长：左 ${left?.duration ?: 0} 秒 · 右 ${right?.duration ?: 0} 秒",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
