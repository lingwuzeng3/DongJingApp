// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // 添加 KSP 插件
    // 须与 gradle/libs.versions.toml 中 kotlin 版本一致，否则 Room 生成的 Dao 实现会与协程签名冲突
    id("com.google.devtools.ksp") version "2.2.10-2.0.2" apply false
}