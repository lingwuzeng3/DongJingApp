package com.example.dongjingapp.data.repository

import com.example.dongjingapp.data.network.NetworkModule
import com.example.dongjingapp.data.network.ProgressRequestBody
import com.example.dongjingapp.data.network.VideoUploadApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody

class VideoUploadRepository(
    private val api: VideoUploadApi = NetworkModule.videoUploadApi
) {

    suspend fun uploadVideoBytes(
        fileName: String,
        bytes: ByteArray,
        mimeType: String = "video/*",
        onProgress: (Int) -> Unit
    ): Result<String> {
        return try {
            val body = ProgressRequestBody(
                mimeType.toMediaTypeOrNull(),
                bytes,
                onProgress
            )
            val part = MultipartBody.Part.createFormData("file", fileName, body)
            val response = api.uploadVideo(part)
            if (response.isSuccessful) {
                Result.success(response.code().toString())
            } else {
                Result.failure(Exception("HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
