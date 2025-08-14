package com.mobile.karadanatv.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.karadanatv.DestinationScreen
import com.mobile.karadanatv.ui.components.BottomNavigationMenuItem
import com.mobile.karadanatv.ui.components.CustomAppBar
import com.mobile.karadanatv.ui.components.CustomNavigationBar
import com.mobile.karadanatv.ui.components.VideosGrid
import com.mobile.karadanatv.ui.theme.Background
import com.mobile.karadanatv.utils.CheckSignedIn
import com.mobile.karadanatv.utils.CheckSignedOut
import com.mobile.karadanatv.utils.ObserveErrorMessage
import com.mobile.karadanatv.utils.navigateTo
import com.mobile.karadanatv.viewmodels.AuthViewModel
import com.mobile.karadanatv.viewmodels.VideoViewModel


@Composable
fun VideoListScreen(
    videoViewModel: VideoViewModel,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val errorMessage by videoViewModel.errorMessage

    ObserveErrorMessage(errorMessage)


    CheckSignedOut(authViewModel,navController)

    val uiState by videoViewModel.uiState.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomAppBar(
                title = "Videolar",
                onRightClicked = { authViewModel.logout() },
                showRightButton = true
            )
        },
        bottomBar = {
            CustomNavigationBar(
                navController = navController,
                selectedItem = BottomNavigationMenuItem.VIDEOS
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(it)
        ) {
            if (uiState.isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                }
            } else if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = { videoViewModel.fetchVideos() }) {
                    Text("Yeniden Dene")
                }
            } else if (uiState.videos.isEmpty()) {
                Text("Gösterilecek video bulunamadı.")
            } else {
                VideosGrid(videos = uiState.videos) { video ->
                    val route = DestinationScreen.VideoPlayer.createRoute(video.url)
                    navigateTo(navController, route)
                }
            }

        }
    }
}