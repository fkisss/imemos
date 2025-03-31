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
import com.imemos.data.model.Memo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoDetailScreen(
    memoId: Int,
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    viewModel: MemoDetailViewModel = hiltViewModel()
) {
    val memoState by viewModel.memoState.collectAsState()
    
    LaunchedEffect(memoId) {
        viewModel.loadMemo(memoId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("笔记详情") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (memoState is MemoDetailState.Success) {
                        IconButton(onClick = { onEditClick(memoId) }) {
                            Icon(Icons.Default.Edit, contentDescription = "编辑")
                        }
                        IconButton(onClick = { viewModel.deleteMemo(memoId, onSuccess = onBackClick) }) {
                            Icon(Icons.Default.Delete, contentDescription = "删除")
                        }
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
            when (val state = memoState) {
                is MemoDetailState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is MemoDetailState.Success -> {
                    MemoContent(memo = state.memo)
                }
                is MemoDetailState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("加载失败: ${state.message}")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadMemo(memoId) }) {
                            Text("重试")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MemoContent(memo: Memo) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 笔记元数据
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val visibilityText = when(memo.visibility) {
                "PRIVATE" -> "私密"
                "PROTECTED" -> "受保护"
                "PUBLIC" -> "公开"
                else -> "私密"
            }
            
            Text(
                text = "可见性: $visibilityText",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (memo.pinned) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.PushPin,
                        contentDescription = "置顶",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "已置顶",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 笔记内容
        Text(
            text = memo.content,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}