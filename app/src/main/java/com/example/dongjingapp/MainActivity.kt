package com.example.dongjingapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dongjingapp.ui.navigation.AppNavigation
import com.example.dongjingapp.ui.theme.DongJingAppTheme

/**
 * 主活动类 - 应用入口点
 * 集成导航组件，实现不同功能模块的切换
 */
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DongJingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    AppContent()
                }
            }
        }
    }
}

/**
 * 应用主内容
 * 集成导航组件，包含所有功能模块
 */
@Composable
fun AppContent() {
    AppNavigation()
}

@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    DongJingAppTheme {
        AppContent()
    }
}