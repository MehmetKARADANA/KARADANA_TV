package com.mobile.karadanatv.ui.screens

import android.content.res.Configuration
import android.view.Surface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.mobile.karadanatv.data.LIVEID
import com.mobile.karadanatv.ui.components.BottomNavigationMenuItem
import com.mobile.karadanatv.ui.components.CustomAppBar
import com.mobile.karadanatv.ui.components.CustomNavigationBar
import com.mobile.karadanatv.ui.theme.Background
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun LiveScreen(
    navController: NavController,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Background),
        topBar = {
            CustomAppBar(title = "Yeşil Deniz", showBackButton = false, onBackClicked = {})
        },
        bottomBar = {
            CustomNavigationBar(
                selectedItem = BottomNavigationMenuItem.LIVE,
                navController = navController
            )
        }) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Background)
                .padding(it),
            verticalArrangement = Arrangement.Center
        ) {
            AndroidView(
                modifier = Modifier,
                factory = { ctx ->
                    val opts = IFramePlayerOptions.Builder()
                        .controls(1)
                        .build()

                    YouTubePlayerView(ctx).apply {
                        enableAutomaticInitialization = false
                        lifecycleOwner.lifecycle.addObserver(this)

                        initialize(object : AbstractYouTubePlayerListener() {
                            override fun onReady(player: YouTubePlayer) {
                                player.loadVideo(LIVEID, 0f)
                                if (isLandscape) {
                                    this@apply.enterFullScreen()
                                } else {
                                    this@apply.exitFullScreen()
                                }
                            }
                        }, opts)
                    }
                }, update = { view ->
                    if (isLandscape) {
                        view.enterFullScreen()
                    } else {
                        view.exitFullScreen()
                    }
                },
                onRelease = { view ->
                    lifecycleOwner.lifecycle.removeObserver(view)
                    view.release()
                }
            )
        }
    }

}
