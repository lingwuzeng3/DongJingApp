package com.example.dongjingapp.ui.video

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dongjingapp.data.repository.VideoUploadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface UploadUiState {
    data object Idle : UploadUiState
    data object Reading : UploadUiState
    data object Uploading : UploadUiState
    data class Success(val message: String) : UploadUiState
    data class Error(val message: String) : UploadUiState
}

class VideoUploadViewModel(
    application: Application,
    private val repository: VideoUploadRepository = VideoUploadRepository()
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<UploadUiState>(UploadUiState.Idle)
    val uiState: StateFlow<UploadUiState> = _uiState.asStateFlow()

    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress.asStateFlow()

    fun reset() {
        _uiState.value = UploadUiState.Idle
        _progress.value = 0
    }

    fun upload(uri: Uri, displayName: String?) {
        viewModelScope.launch {
            _uiState.value = UploadUiState.Reading
            _progress.value = 0
            val ctx = getApplication<Application>()
            val bytes = withContext(Dispatchers.IO) {
                ctx.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            }
            if (bytes == null || bytes.isEmpty()) {
                _uiState.value = UploadUiState.Error("无法读取视频文件")
                return@launch
            }
            val name = displayName?.takeIf { it.isNotBlank() } ?: "upload_${System.currentTimeMillis()}.mp4"
            _uiState.value = UploadUiState.Uploading
            val result = withContext(Dispatchers.IO) {
                repository.uploadVideoBytes(
                    fileName = name,
                    bytes = bytes,
                    mimeType = ctx.contentResolver.getType(uri) ?: "video/*",
                    onProgress = { p -> _progress.value = p }
                )
            }
            _uiState.value = result.fold(
                onSuccess = { UploadUiState.Success("上传完成（演示服务器 HTTP $it）") },
                onFailure = { UploadUiState.Error(it.message ?: "上传失败") }
            )
        }
    }

    companion object {
        fun factory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return VideoUploadViewModel(application) as T
                }
            }
    }
}
