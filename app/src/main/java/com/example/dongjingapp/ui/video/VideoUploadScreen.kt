package com.example.dongjingapp.ui.video

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoUploadScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: VideoUploadViewModel = viewModel(
        factory = VideoUploadViewModel.factory(application)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()

    var pickedLabel by remember { mutableStateOf<String?>(null) }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        pickedLabel = queryDisplayName(context, uri)
        viewModel.upload(uri, pickedLabel)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("视频上传") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B82F6),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "选择本地视频后上传到演示服务器（httpbin.org），用于验证 Retrofit 多段上传与进度。",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B7280)
            )
            Button(
                onClick = { picker.launch("video/*") },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is UploadUiState.Reading && uiState !is UploadUiState.Uploading
            ) {
                Text("选择视频")
            }
            pickedLabel?.let { Text(text = "已选：$it", style = MaterialTheme.typography.bodySmall) }

            when (val s = uiState) {
                UploadUiState.Idle -> {}
                UploadUiState.Reading -> Text("正在读取文件…")
                UploadUiState.Uploading -> {
                    Text("上传中 $progress%")
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF3B82F6),
                        trackColor = Color(0xFFE5E7EB)
                    )
                }
                is UploadUiState.Success -> Text(
                    text = s.message,
                    color = Color(0xFF059669)
                )
                is UploadUiState.Error -> Text(
                    text = s.message,
                    color = Color(0xFFDC2626)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.reset()
                    pickedLabel = null
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is UploadUiState.Reading && uiState !is UploadUiState.Uploading
            ) {
                Text("清除状态")
            }
        }
    }
}

private fun queryDisplayName(context: android.content.Context, uri: Uri): String? {
    val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
    return context.contentResolver.query(uri, projection, null, null, null)?.use { c ->
        val idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (idx < 0 || !c.moveToFirst()) null else c.getString(idx)
    }
}
