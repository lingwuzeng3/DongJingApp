package com.example.dongjingapp.data.service

/**
 * AR 训练辅助逻辑（姿态检测主体在 [com.example.dongjingapp.ui.ar.ARScreen] + MediaPipe）
 */
class ARService {

    fun startPoseEstimation() {
        // 预览与 PoseLandmarker 在 UI 层绑定生命周期
    }

    fun stopPoseEstimation() {
        // 与 start 成对，由 Composable DisposableEffect 释放
    }

    fun recognizeAction(poseLandmarks: List<Any>): String {
        return "演示：请面向前置摄像头，保持全身在画面中"
    }

    fun evaluateActionQuality(poseLandmarks: List<Any>, targetAction: String): Float {
        return 0.75f
    }

    fun getActionGuidance(action: String, error: String): String {
        return "保持核心收紧，动作放慢，对照画面中的骨架线调整关节角度"
    }
}
