package com.example.dongjingapp.data

/**
 * 统一视频地址配置。
 * 后续切换云端时，优先改 [VIDEO_BASE_URL]。
 */
object DemoMedia {
    /**
     * 你自己的标准视频云端域名（示例值）。
     * 可以替换为 CDN/OSS/COS 域名，如：
     * https://cdn.example.com/videos/
     */
    const val VIDEO_BASE_URL = "https://storage.googleapis.com/"

    // 课程/视频服务只维护相对路径，方便后续批量切换域名。
    const val SAMPLE_PATH_SHORT = "exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
    const val SAMPLE_PATH_ALT = "exoplayer-test-media-1/mp4/android-screens-10s.mp4"

    val SAMPLE_MP4_SHORT = resolveVideoUrl(SAMPLE_PATH_SHORT)
    val SAMPLE_MP4_ALT = resolveVideoUrl(SAMPLE_PATH_ALT)

    fun resolveVideoUrl(pathOrUrl: String): String {
        if (pathOrUrl.startsWith("http://") || pathOrUrl.startsWith("https://")) {
            return pathOrUrl
        }
        val base = VIDEO_BASE_URL.trimEnd('/')
        val path = pathOrUrl.trimStart('/')
        return "$base/$path"
    }
}
