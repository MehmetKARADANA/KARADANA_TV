package com.mobile.karadanatv.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobile.karadanatv.data.VideoItem

@Composable
fun VideosGrid(
    videos: List<VideoItem>,
    onClick: (VideoItem) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(12.dp)) {
        items(videos) { v ->
            VideoCard(
                item = v,
                modifier = Modifier.padding(8.dp),
                onClick = { onClick(v) }
            )
        }
    }
}