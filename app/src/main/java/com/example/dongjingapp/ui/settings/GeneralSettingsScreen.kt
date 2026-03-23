package com.example.dongjingapp.ui.settings

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dongjingapp.data.settings.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSettingsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as Application
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.factory(app))
    val state by settingsViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("通用设置") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B82F6),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            ListItem(
                leadingContent = { Icon(Icons.Filled.DarkMode, contentDescription = null) },
                headlineContent = { Text("主题模式") },
                supportingContent = { Text("跟随系统 / 浅色 / 深色") }
            )
            ThemeChoiceRow(
                label = "跟随系统",
                selected = state.themeMode == ThemeMode.SYSTEM,
                onSelect = { settingsViewModel.setThemeMode(ThemeMode.SYSTEM) }
            )
            ThemeChoiceRow(
                label = "浅色模式",
                selected = state.themeMode == ThemeMode.LIGHT,
                onSelect = { settingsViewModel.setThemeMode(ThemeMode.LIGHT) }
            )
            ThemeChoiceRow(
                label = "深色模式",
                selected = state.themeMode == ThemeMode.DARK,
                onSelect = { settingsViewModel.setThemeMode(ThemeMode.DARK) }
            )

            ListItem(
                leadingContent = { Icon(Icons.Filled.Language, contentDescription = null) },
                headlineContent = { Text("语言") },
                supportingContent = { Text("中文（预留多语言结构）") }
            )

            ListItem(
                leadingContent = { Icon(Icons.Filled.PlayArrow, contentDescription = null) },
                headlineContent = { Text("自动播放视频") },
                trailingContent = {
                    Switch(
                        checked = state.autoPlayVideo,
                        onCheckedChange = settingsViewModel::setAutoPlayVideo
                    )
                }
            )
            ListItem(
                headlineContent = { Text("仅 Wi-Fi 下自动播放") },
                supportingContent = { Text("移动网络将默认不自动播放") },
                trailingContent = {
                    Switch(
                        checked = state.wifiOnlyAutoPlay,
                        onCheckedChange = settingsViewModel::setWifiOnlyAutoPlay
                    )
                }
            )
        }
    }
}

@Composable
private fun ThemeChoiceRow(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp, top = 12.dp)
        )
    }
}
