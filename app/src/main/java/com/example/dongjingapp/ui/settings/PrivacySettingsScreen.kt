package com.example.dongjingapp.ui.settings

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySettingsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val cameraGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
    val mediaGranted = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_MEDIA_VIDEO
    ) == PackageManager.PERMISSION_GRANTED
    var showClearDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("隐私设置") },
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
                leadingContent = { Icon(Icons.Filled.CameraAlt, contentDescription = null) },
                headlineContent = { Text("相机权限") },
                supportingContent = { Text(if (cameraGranted) "已授权" else "未授权") },
                trailingContent = {
                    TextButton(onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.parse("package:${context.packageName}"))
                        )
                    }) { Text("管理") }
                }
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.Folder, contentDescription = null) },
                headlineContent = { Text("视频读取权限") },
                supportingContent = { Text(if (mediaGranted) "已授权" else "未授权") },
                trailingContent = {
                    TextButton(onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.parse("package:${context.packageName}"))
                        )
                    }) { Text("管理") }
                }
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.Description, contentDescription = null) },
                headlineContent = { Text("隐私政策") },
                supportingContent = { Text("查看应用隐私政策（占位页）") }
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.Gavel, contentDescription = null) },
                headlineContent = { Text("用户协议") },
                supportingContent = { Text("查看服务条款（占位页）") }
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.Delete, contentDescription = null, tint = Color(0xFFDC2626)) },
                headlineContent = { Text("清除本地数据", color = Color(0xFFDC2626)) },
                supportingContent = { Text("仅清理本地缓存/偏好（演示提示）") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showClearDialog = true }
            )
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("确认清除本地数据") },
            text = { Text("该操作不可撤销，是否继续？（当前为演示提示）") },
            confirmButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("确认清除") }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("取消") }
            }
        )
    }
}
