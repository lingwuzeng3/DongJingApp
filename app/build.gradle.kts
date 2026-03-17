plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // 移除 kotlin("kapt")，改为添加 ksp 插件
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
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
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // 导航组件
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // 本地数据存储 - 使用 ksp 替代 kapt
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")  // 原来是 kapt
    implementation("androidx.room:room-ktx:2.6.1")

    // 网络请求
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // 添加 OkHttp 日志拦截器（可选）
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // 视频播放
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.19.1")

    // 姿态估计 - MediaPipe Tasks Vision
    implementation("com.google.mediapipe:mediapipe-tasks-vision:0.10.9")

    // AR功能 - ARCore
    implementation("com.google.ar:core:1.37.0")
    // 添加 Sceneform 用于 AR 渲染（可选）
    implementation("com.google.ar.sceneform:sceneform:1.17.1")

    // 图片加载（推荐添加）
    implementation("io.coil-kt:coil-compose:2.6.0")

    // 权限处理（推荐添加）
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    // 测试依赖
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}