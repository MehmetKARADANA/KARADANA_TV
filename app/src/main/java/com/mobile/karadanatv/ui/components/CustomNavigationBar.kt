package com.mobile.karadanatv.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.karadanatv.DestinationScreen
import com.mobile.karadanatv.R
import com.mobile.karadanatv.ui.theme.Background
import com.mobile.karadanatv.utils.navigateTo


enum class BottomNavigationMenuItem(
    val icon: Int,
    val navDestinationScreen: DestinationScreen,
) {
    VIDEOS(R.drawable.video, DestinationScreen.VideoList),
    LIVE(R.drawable.liveicon, DestinationScreen.LiveScreen)
}
@Composable
fun CustomNavigationBar(selectedItem: BottomNavigationMenuItem, navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(top = 8.dp)) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
                .background(Background)
        ) {
            for (item in BottomNavigationMenuItem.entries) {
                Image(
                    painter = painterResource(item.icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                        .weight(1f)
                        .clickable {
                            navigateTo(navController, item.navDestinationScreen.route)
                        },
                    colorFilter = if (item == selectedItem)
                        ColorFilter.tint(Color.Black)
                    else
                        ColorFilter.tint(Color.Gray)
                )
            }
        }
    }
}