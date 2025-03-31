package com.imemos.ui.memo

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoEditScreen(
    memoId: Int? = null,
    onBackClick: () -> Unit,
    viewModel: MemoEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showVisibilityDialog by remember { mutableStateOf(false) }
    
    // 加载现有笔记（如果是编辑模式）
    LaunchedEffect(memoId) {
        memoId?.let { viewModel.loadMemo(it) }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (memoId == null) "新建笔记" else "编辑笔记") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 可见性选择
                    IconButton(onClick = { showVisibilityDialog = true }) {
                        val icon = when(uiState.visibility) {
                            "PRIVATE" -> Icons.Default.Lock
                            "PROTECTED" -> Icons.Default.Security
                            "PUBLIC" -> Icons.Default.Public
                            else -> Icons.Default.Lock
                        }
                        Icon(icon, contentDescription = "设置可见性")
                    }
                    
                    // 保存按钮
                    IconButton(
                        onClick = { 
                            if (memoId == null) {
                                viewModel.createMemo(onSuccess = onBackClick)
                            } else {
                                viewModel.updateMemo(memoId, onSuccess = onBackClick)
                            }
                        },
                        enabled = uiState.content.isNotBlank() && !uiState.isLoading
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "保存")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // 内容编辑区
                OutlinedTextField(
                    value = uiState.content,
                    onValueChange = { viewModel.updateContent(it) },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    placeholder = { Text("在这里输入笔记内容...") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
            
            // 加载指示器
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
    
    // 可见性选择对话框
    if (showVisibilityDialog) {
        AlertDialog(
            onDismissRequest = { showVisibilityDialog = false },
            title = { Text("设置笔记可见性") },
            text = {
                Column {
                    VisibilityOption(
                        title = "私密",
                        description = "仅自己可见",
                        icon = Icons.Default.Lock,
                        isSelected = uiState.visibility == "PRIVATE",
                        onClick = {
                            viewModel.updateVisibility("PRIVATE")
                            showVisibilityDialog = false
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    VisibilityOption(
                        title = "受保护",
                        description = "登录用户可见",
                        icon = Icons.Default.Security,
                        isSelected = uiState.visibility == "PROTECTED",
                        onClick = {
                            viewModel.updateVisibility("PROTECTED")
                            showVisibilityDialog = false
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    VisibilityOption(
                        title = "公开",
                        description = "所有人可见",
                        icon = Icons.Default.Public,
                        isSelected = uiState.visibility == "PUBLIC",
                        onClick = {
                            viewModel.updateVisibility("PUBLIC")
                            showVisibilityDialog = false
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showVisibilityDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
fun VisibilityOption(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "已选择",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}