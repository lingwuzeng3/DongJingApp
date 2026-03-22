plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.2.10-2.0.2"
}

android {
    namespace = "com.example.dongjingapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.dongjingapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // 基础依赖
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // 协程依赖
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // 【修改点 1】：强制将 Compose BOM 更新到 2024.10.00，以支持 PullToRefreshBox
    // 替换了原有的 implementation(platform(libs.androidx.compose.bom))
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // 这里的 material3 会自动跟随上面的 BOM 版本升级到 1.3.0+
    implementation(libs.androidx.compose.material3)

    // 添加缺失的依赖
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.foundation:foundation")

    // 导航组件
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // 本地数据存储（与 Kotlin 2.2 + KSP 2.0.x 配套使用较新版本）
    implementation("androidx.room:room-runtime:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")

    // 网络请求
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    // 添加 OkHttp 日志拦截器（可选）
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // 视频播放（Media3，替代已弃用的 com.google.android.exoplayer2）
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")

    // CameraX（AR 预览 / 分析）
    val cameraX = "1.4.1"
    implementation("androidx.camera:camera-core:$cameraX")
    implementation("androidx.camera:camera-camera2:$cameraX")
    implementation("androidx.camera:camera-lifecycle:$cameraX")
    implementation("androidx.camera:camera-view:$cameraX")

    // 姿态估计 - MediaPipe Tasks Vision
    implementation("com.google.mediapipe:tasks-vision:0.10.14")

    // AR功能 - ARCore
    // 暂时注释掉，避免依赖解析失败
    // implementation("com.google.ar:core:1.37.0")
    // 添加 Sceneform 用于 AR 渲染（可选）
    // implementation("com.google.ar.sceneform:sceneform:1.17.1")

    // 图片加载（推荐添加）
    implementation("io.coil-kt:coil-compose:2.6.0")

    // 权限处理（推荐添加）
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    // 测试依赖
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // 【修改点 2】：测试环境的 BOM 也同步强制更新
    // 替换了原有的 androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.00"))

    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}