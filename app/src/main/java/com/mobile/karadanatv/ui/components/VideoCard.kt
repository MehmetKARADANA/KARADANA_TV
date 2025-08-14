package com.mobile.karadanatv.ui.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.mobile.karadanatv.data.VideoItem
import com.mobile.karadanatv.utils.resolvePlayableUrl

@Composable
fun VideoCard(
    item: VideoItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val ctx = LocalContext.current

    var thumbUrl by remember(item.thumbnail) { mutableStateOf<String?>(item.thumbnail) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    println("log thumbnail card $thumbUrl and ${item.thumbnail}")
    val request = remember(thumbUrl) {
        ImageRequest.Builder(ctx)
            .data(thumbUrl ?: "")
            .crossfade(true)
            .listener(
                onStart = { errorMsg = null },
                onError = { _, result ->
                    errorMsg = result.throwable.message
                    Log.e(
                        "Coil/Thumb",
                        "Thumbnail yüklenemedi: $thumbUrl | Hata: ${result.throwable}"
                    )
                }
            )
            .build()
    }

    val painter = rememberAsyncImagePainter(model = request)

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Box {
            Image(
                painter = painter,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.0f to Color.Transparent,
                            0.6f to Color(0x99000000),
                            1.0f to Color(0xCC000000)
                        )
                    )
            )
            Log.d("Thumbnail", "e $errorMsg")
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = item.title,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

