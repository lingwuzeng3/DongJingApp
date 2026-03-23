package com.example.dongjingapp.ui.settings

import android.app.Application
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.dongjingapp.data.service.UserService
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as Application
    val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.factory(app))
    val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val user = UserService().getCurrentUser()
    val displayName = settingsState.profileName.ifBlank { user.username }
    val displayEmail = settingsState.profileEmail.ifBlank { user.email }
    val displayAvatar = settingsState.profileAvatar.ifBlank { user.avatar.orEmpty() }
    val displayHeight = settingsState.profileHeight ?: user.height
    val displayWeight = settingsState.profileWeight ?: user.weight
    val displayGoal = settingsState.profileFitnessGoal.ifBlank { user.fitnessGoal.orEmpty() }

    var showEditNameDialog by remember { mutableStateOf(false) }
    var showEditEmailDialog by remember { mutableStateOf(false) }
    var showEditBodyDialog by remember { mutableStateOf(false) }
    var draftName by remember(displayName) { mutableStateOf(displayName) }
    var draftEmail by remember(displayEmail) { mutableStateOf(displayEmail) }
    var draftHeight by remember(displayHeight) { mutableStateOf(displayHeight?.toString().orEmpty()) }
    var draftWeight by remember(displayWeight) { mutableStateOf(displayWeight?.toString().orEmpty()) }
    var draftGoal by remember(displayGoal) { mutableStateOf(displayGoal) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            settingsViewModel.setProfileAvatar(it.toString())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("账号设置") },
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
            modifier = Modifier.padding(padding)
        ) {
            ListItem(
                leadingContent = {
                    if (displayAvatar.isNotBlank()) {
                        AsyncImage(
                            model = displayAvatar,
                            contentDescription = "头像",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Icon(Icons.Filled.AccountCircle, contentDescription = null)
                    }
                },
                headlineContent = { Text("头像") },
                supportingContent = { Text("上传并更换头像") },
                trailingContent = { Icon(Icons.Filled.PhotoCamera, contentDescription = null, tint = Color(0xFF3B82F6)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { imagePicker.launch(arrayOf("image/*")) }
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.Person, contentDescription = null) },
                headlineContent = { Text("昵称") },
                supportingContent = { Text(displayName) },
                trailingContent = { Text("编辑", color = Color(0xFF3B82F6)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        draftName = displayName
                        showEditNameDialog = true
                    }
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.Email, contentDescription = null) },
                headlineContent = { Text("邮箱") },
                supportingContent = { Text(displayEmail) },
                trailingContent = { Text("编辑", color = Color(0xFF3B82F6)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        draftEmail = displayEmail
                        showEditEmailDialog = true
                    }
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.AccountCircle, contentDescription = null) },
                headlineContent = { Text("身体数据") },
                supportingContent = {
                    Text(
                        "身高 ${displayHeight?.let { String.format("%.1f", it) } ?: "--"} cm · " +
                            "体重 ${displayWeight?.let { String.format("%.1f", it) } ?: "--"} kg · " +
                            "目标 ${if (displayGoal.isBlank()) "未设置" else displayGoal}"
                    )
                },
                trailingContent = { Text("编辑", color = Color(0xFF3B82F6)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        draftHeight = displayHeight?.toString().orEmpty()
                        draftWeight = displayWeight?.toString().orEmpty()
                        draftGoal = displayGoal
                        showEditBodyDialog = true
                    }
            )
            ListItem(
                leadingContent = { Icon(Icons.Filled.Lock, contentDescription = null) },
                headlineContent = { Text("修改密码") },
                supportingContent = { Text("功能预留，接入账号系统后开放") }
            )
        }
    }

    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = { Text("编辑昵称") },
            text = {
                OutlinedTextField(
                    value = draftName,
                    onValueChange = { draftName = it },
                    singleLine = true,
                    label = { Text("昵称") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        settingsViewModel.setProfileName(draftName)
                        showEditNameDialog = false
                    },
                    enabled = draftName.isNotBlank()
                ) { Text("保存") }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) { Text("取消") }
            }
        )
    }

    if (showEditEmailDialog) {
        val emailValid = draftEmail.contains("@") && draftEmail.contains(".")
        AlertDialog(
            onDismissRequest = { showEditEmailDialog = false },
            title = { Text("编辑邮箱") },
            text = {
                OutlinedTextField(
                    value = draftEmail,
                    onValueChange = { draftEmail = it },
                    singleLine = true,
                    label = { Text("邮箱") },
                    isError = draftEmail.isNotBlank() && !emailValid
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        settingsViewModel.setProfileEmail(draftEmail)
                        showEditEmailDialog = false
                    },
                    enabled = draftEmail.isNotBlank() && emailValid
                ) { Text("保存") }
            },
            dismissButton = {
                TextButton(onClick = { showEditEmailDialog = false }) { Text("取消") }
            }
        )
    }

    if (showEditBodyDialog) {
        val parsedHeight = draftHeight.toFloatOrNull()
        val parsedWeight = draftWeight.toFloatOrNull()
        val validHeight = parsedHeight == null || parsedHeight in 50f..260f
        val validWeight = parsedWeight == null || parsedWeight in 20f..300f
        AlertDialog(
            onDismissRequest = { showEditBodyDialog = false },
            title = { Text("编辑身体数据") },
            text = {
                Column {
                    OutlinedTextField(
                        value = draftHeight,
                        onValueChange = { draftHeight = it },
                        singleLine = true,
                        label = { Text("身高(cm)") },
                        isError = draftHeight.isNotBlank() && !validHeight
                    )
                    OutlinedTextField(
                        value = draftWeight,
                        onValueChange = { draftWeight = it },
                        singleLine = true,
                        label = { Text("体重(kg)") },
                        isError = draftWeight.isNotBlank() && !validWeight
                    )
                    OutlinedTextField(
                        value = draftGoal,
                        onValueChange = { draftGoal = it },
                        singleLine = true,
                        label = { Text("健身目标") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        settingsViewModel.setProfileBodyData(
                            heightCm = parsedHeight,
                            weightKg = parsedWeight,
                            fitnessGoal = draftGoal
                        )
                        showEditBodyDialog = false
                    },
                    enabled = validHeight && validWeight
                ) { Text("保存") }
            },
            dismissButton = {
                TextButton(onClick = { showEditBodyDialog = false }) { Text("取消") }
            }
        )
    }
}
