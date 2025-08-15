package com.mobile.karadanatv

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobile.karadanatv.DestinationScreen.Login
import com.mobile.karadanatv.ui.screens.LiveScreen
import com.mobile.karadanatv.ui.screens.LoginScreen
import com.mobile.karadanatv.ui.screens.SignUpScreen
import com.mobile.karadanatv.ui.screens.VideoListScreen
import com.mobile.karadanatv.ui.screens.VideoPlayerScreen
import com.mobile.karadanatv.ui.theme.KaradanaTVTheme
import com.mobile.karadanatv.viewmodels.AuthViewModel
import com.mobile.karadanatv.viewmodels.VideoViewModel


//2. Number of Screens – The app should include exactly 5 screens: Login, Sign-Up, Video
//List, Video Player, and Live Streaming.

sealed class DestinationScreen(var route: String) {
    data object Login : DestinationScreen("login")
    data object SignUp : DestinationScreen("signup")
    data object VideoList : DestinationScreen("videolist")
    data object VideoPlayer : DestinationScreen("video/{url}") {
        fun createRoute(url: String) = "video/${Uri.encode(url)}"
    }

    data object LiveScreen : DestinationScreen("live")
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KaradanaTVTheme {
                AppNavigation()
            }
        }
    }

    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()
        val authViewModel: AuthViewModel by viewModels()
        val videoViewModel: VideoViewModel by viewModels()


        NavHost(
            navController = navController,
            startDestination = if (authViewModel.signIn.value) DestinationScreen.VideoList.route else DestinationScreen.Login.route
        ) {
            composable(DestinationScreen.Login.route) {
                LoginScreen(navController, authViewModel)
            }
            composable(DestinationScreen.SignUp.route) {
                SignUpScreen(navController, authViewModel)
            }

            composable(DestinationScreen.VideoList.route) {
                VideoListScreen(
                    navController = navController,
                    videoViewModel = videoViewModel,
                    authViewModel = authViewModel
                )
            }

            composable(DestinationScreen.VideoPlayer.route) {
                val url = it.arguments?.getString("url")
                url?.let {
                    Log.d("URL", "url $it")
                    VideoPlayerScreen(videoUrl = it, navController)
                }
            }

            composable(DestinationScreen.LiveScreen.route) {
                LiveScreen(navController,authViewModel)
            }
        }
    }

}