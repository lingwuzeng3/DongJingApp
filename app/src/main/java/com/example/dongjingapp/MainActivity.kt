package com.example.dongjingapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dongjingapp.data.repository.SettingsRepository
import com.example.dongjingapp.data.settings.AppSettings
import com.example.dongjingapp.data.settings.ThemeMode
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
            AppRootTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    AppContent()
                }
            }
        }
    }
}

@Composable
private fun AppRootTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val repository = SettingsRepository(context.applicationContext)
    val appSettings by repository.settingsFlow.collectAsStateWithLifecycle(
        initialValue = AppSettings()
    )
    val darkTheme = when (appSettings.themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    DongJingAppTheme(darkTheme = darkTheme, content = content)
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