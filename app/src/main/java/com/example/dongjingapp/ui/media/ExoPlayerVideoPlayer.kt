package com.example.dongjingapp.ui.media

import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@Composable
fun rememberExoPlayer(
    videoUri: String,
    playWhenReady: Boolean = true
): ExoPlayer {
    val context = LocalContext.current
    val player = remember(videoUri, playWhenReady) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            repeatMode = Player.REPEAT_MODE_OFF
            this.playWhenReady = playWhenReady
            prepare()
        }
    }
    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }
    return player
}

@Composable
fun ExoPlayerVideoView(
    player: ExoPlayer,
    modifier: Modifier = Modifier,
    useController: Boolean = true
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            PlayerView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                this.player = player
                this.useController = useController
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            }
        },
        update = { it.player = player }
    )
}
