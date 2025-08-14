package com.mobile.karadanatv.viewmodels

import android.provider.MediaStore.Video
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.karadanatv.data.VIDEOS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.toObjects
import com.mobile.karadanatv.data.VideoItem
import com.mobile.karadanatv.utils.resolvePlayableUrl
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

data class VideoListUiState(
    val videos: List<VideoItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class VideoViewModel : BaseViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(VideoListUiState())
    val uiState: StateFlow<VideoListUiState> = _uiState.asStateFlow()


    init {
        fetchVideos()
    }

fun fetchVideos(limit: Long = 50) {
    _uiState.update { it.copy(isLoading = true, errorMessage = null) }

    viewModelScope.launch {
        try {
            val snapshot = db.collection(VIDEOS)
                .limit(limit)
                .get()
                .await()

            val raw = snapshot.toObjects<VideoItem>().mapIndexed { idx, v ->

                v.copy(id = snapshot.documents[idx].id)
            }
            val cleaned = raw.map { v ->
                async {
                    val httpsUrl = resolvePlayableUrl(v.url)
                    val httpsThumb = resolvePlayableUrl(v.thumbnail)
                    v.copy(url = httpsUrl, thumbnail = httpsThumb)
                }
            }.awaitAll()

            _uiState.update { it.copy(videos = cleaned, isLoading = false, errorMessage = null) }

        } catch (e: Exception) {
            _uiState.update {
                it.copy(isLoading = false, errorMessage = "Videolar yüklenirken hata: ${e.localizedMessage}")
            }
            handleException(customMessage = "Videolar yüklenirken hata: ${e.localizedMessage}")
        }
    }
}
}
