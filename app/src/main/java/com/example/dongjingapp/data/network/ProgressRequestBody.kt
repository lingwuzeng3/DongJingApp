package com.example.dongjingapp.data.network

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import kotlin.math.min

/**
 * 带上传进度的 RequestBody，用于大文件 multipart。
 */
class ProgressRequestBody(
    private val contentType: MediaType?,
    private val data: ByteArray,
    private val onProgress: (percent: Int) -> Unit
) : RequestBody() {

    override fun contentType(): MediaType? = contentType

    override fun contentLength(): Long = data.size.toLong()

    override fun writeTo(sink: BufferedSink) {
        val chunk = 16 * 1024
        var offset = 0
        val total = data.size
        while (offset < total) {
            val len = min(chunk, total - offset)
            sink.write(data, offset, len)
            offset += len
            onProgress((100L * offset / total).toInt().coerceIn(0, 100))
        }
    }
}
