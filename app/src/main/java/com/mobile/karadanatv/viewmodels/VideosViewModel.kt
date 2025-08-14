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

    fun fetchVideos() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                val snapshot = db.collection(VIDEOS).get().await()
                val videoList = snapshot.toObjects<VideoItem>()
                _uiState.update {
                    it.copy(
                        videos = videoList,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Videolar yüklenirken hata oluştu: ${e.localizedMessage}"
                    )
                }
                handleException(customMessage = "Videolar yüklenirken hata oluştu: ${e.localizedMessage}")
            }
        }
    }
}
