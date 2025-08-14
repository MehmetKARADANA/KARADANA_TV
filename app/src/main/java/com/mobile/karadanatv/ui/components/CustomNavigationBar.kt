package com.mobile.karadanatv.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.karadanatv.DestinationScreen
import com.mobile.karadanatv.ui.theme.Background
import com.mobile.karadanatv.utils.navigateTo


enum class BottomNavigationMenuItem(
    val icon: ImageVector,
    val navDestinationScreen: DestinationScreen,
) {
    VIDEOS(Icons.Default.PlayArrow, DestinationScreen.VideoList),
    LIVE(Icons.Default.Star, DestinationScreen.LiveScreen)
}
@Composable
fun CustomNavigationBar(selectedItem: BottomNavigationMenuItem, navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(top = 8.dp)/*.shadow(12.dp)*/) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
                .background(Background)
        ) {
            for (item in BottomNavigationMenuItem.entries) {
                Image(
                    imageVector = item.icon,
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