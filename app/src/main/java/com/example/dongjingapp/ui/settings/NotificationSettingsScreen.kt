package com.example.dongjingapp.ui.settings

import android.app.Application
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as Application
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.factory(app))
    val state by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val notificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("通知设置") },
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
                leadingContent = { Icon(Icons.Filled.Notifications, contentDescription = null) },
                headlineContent = { Text("系统通知权限") },
                supportingContent = {
                    Text(if (notificationsEnabled) "已开启" else "未开启，请前往系统设置授权")
                },
                trailingContent = {
                    TextButton(onClick = {
                        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        } else {
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(android.net.Uri.parse("package:${context.packageName}"))
                        }
                        context.startActivity(intent)
                    }) { Text("去设置") }
                }
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.Notifications, contentDescription = null) },
                headlineContent = { Text("训练提醒") },
                supportingContent = { Text("每日提醒坚持训练") },
                trailingContent = {
                    Switch(
                        checked = state.trainingReminderEnabled,
                        onCheckedChange = settingsViewModel::setTrainingReminderEnabled
                    )
                }
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.Schedule, contentDescription = null) },
                headlineContent = { Text("提醒时间") },
                supportingContent = { Text(state.reminderText) }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = {
                        val next = (state.reminderHour + 23) % 24
                        settingsViewModel.setReminderTime(next, state.reminderMinute)
                    }
                ) { Text("小时 -") }
                TextButton(
                    onClick = {
                        val next = (state.reminderHour + 1) % 24
                        settingsViewModel.setReminderTime(next, state.reminderMinute)
                    }
                ) { Text("小时 +") }
                TextButton(
                    onClick = {
                        val next = (state.reminderMinute + 55) % 60
                        settingsViewModel.setReminderTime(state.reminderHour, next)
                    }
                ) { Text("分钟 -") }
                TextButton(
                    onClick = {
                        val next = (state.reminderMinute + 5) % 60
                        settingsViewModel.setReminderTime(state.reminderHour, next)
                    }
                ) { Text("分钟 +") }
            }
        }
    }
}
