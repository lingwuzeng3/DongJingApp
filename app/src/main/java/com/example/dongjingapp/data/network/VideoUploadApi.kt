package com.example.dongjingapp.data.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * 演示用上传接口（https://httpbin.org 接受任意 multipart 并返回 JSON）
 * 正式环境请替换为业务 baseUrl 与契约。
 */
interface VideoUploadApi {

    @Multipart
    @POST("post")
    suspend fun uploadVideo(@Part file: MultipartBody.Part): Response<ResponseBody>
}
