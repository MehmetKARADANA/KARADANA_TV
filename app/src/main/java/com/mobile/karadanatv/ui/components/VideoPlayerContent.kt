package com.mobile.karadanatv.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayerContent(
    videoUrl: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
    var errorText by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(videoUrl) {
        if (videoUrl.isNullOrBlank()) {
            exoPlayer?.release()
            exoPlayer = null
            errorText = "Geçersiz URL"
            return@LaunchedEffect
        }

        if (exoPlayer?.currentMediaItem?.mediaId != videoUrl) {
            exoPlayer?.release()
            exoPlayer = null
        }

        if (exoPlayer == null) {
            val player = ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.Builder()
                    .setUri(videoUrl)
                    .setMediaId(videoUrl)
                    .build()
                setMediaItem(mediaItem)

                addListener(object : Player.Listener {
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        android.util.Log.e("VideoPlayer", "Playback error: ${error.message}", error)
                        errorText = "Oynatma hatası: ${error.errorCodeName} - ${error.message}"
                    }
                    override fun onPlaybackStateChanged(state: Int) {
                        when (state) {
                            Player.STATE_BUFFERING -> android.util.Log.d("VideoPlayer", "BUFFERING")
                            Player.STATE_READY -> { errorText = null; android.util.Log.d("VideoPlayer", "READY") }
                            Player.STATE_ENDED -> android.util.Log.d("VideoPlayer", "ENDED")
                            Player.STATE_IDLE -> android.util.Log.d("VideoPlayer", "IDLE")
                        }
                    }
                })

                prepare()
                playWhenReady = true
            }
            exoPlayer = player
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer?.release(); exoPlayer = null }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, exoPlayer) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer?.pause()
                Lifecycle.Event.ON_RESUME -> if (exoPlayer?.playbackState == Player.STATE_READY) exoPlayer?.play()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (!videoUrl.isNullOrBlank() && exoPlayer != null) {
            AndroidView(
                factory = { ctx -> PlayerView(ctx).apply { player = exoPlayer; useController = true } },
                modifier = Modifier.fillMaxSize(),
                update = { view -> if (view.player != exoPlayer) view.player = exoPlayer }
            )
        } else {
            Text(errorText ?: "Video yüklenemiyor.")
        }
    }
}
