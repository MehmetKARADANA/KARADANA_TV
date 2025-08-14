package com.mobile.karadanatv.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mobile.karadanatv.DestinationScreen
import com.mobile.karadanatv.ui.components.BottomNavigationMenuItem
import com.mobile.karadanatv.ui.components.CustomAppBar
import com.mobile.karadanatv.ui.components.VideoPlayerContent
import com.mobile.karadanatv.ui.theme.Background
import com.mobile.karadanatv.utils.navigateTo

@Composable
fun VideoPlayerScreen(videoUrl : String,navController: NavController) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = { CustomAppBar(title = "Video", showBackButton = true,
            onBackClicked = {
                navigateTo(navController, DestinationScreen.VideoList.route)
            }) }){
        Surface (modifier = Modifier.background(Background).fillMaxSize().padding(it)){
            VideoPlayerContent(videoUrl=videoUrl)
        }
    }
}