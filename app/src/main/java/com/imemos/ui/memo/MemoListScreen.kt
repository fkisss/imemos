package com.imemos.ui.memo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imemos.data.model.Memo
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoListScreen(
    onMemoClick: (Int) -> Unit,
    onCreateMemoClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: MemoListViewModel = hiltViewModel()
) {
    val memoListState by viewModel.memoListState.collectAsState()
    val currentVisibilityFilter by viewModel.currentVisibilityFilter.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("iMemos") },
                actions = {
                    IconButton(onClick = { onSettingsClick() }) {
                        Icon(Icons.Default.Settings, contentDescription = "设置")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateMemoClick() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "新建笔记")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 筛选选项
            FilterChips(
                currentFilter = currentVisibilityFilter,
                onFilterSelected = { viewModel.setVisibilityFilter(it) }
            )
            
            when (val state = memoListState) {
                is MemoListState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is MemoListState.Success -> {
                    if (state.memos.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("没有笔记", style = MaterialTheme.typography.bodyLarge)
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.memos) { memo ->
                                MemoItem(
                                    memo = memo,
                                    onClick = { onMemoClick(memo.id) }
                                )
                            }
                        }
                    }
                }
                is MemoListState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("加载失败: ${state.message}")
                            Button(onClick = { viewModel.loadMemos() }) {
                                Text("重试")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChips(
    currentFilter: String?,
    onFilterSelected: (String?) -> Unit
) {
    val filters = listOf(
        null to "全部",
        "PRIVATE" to "私密",
        "PROTECTED" to "受保护",
        "PUBLIC" to "公开"
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { (filter, label) ->
            FilterChip(
                selected = currentFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(label) },
                leadingIcon = if (currentFilter == filter) {
                    { Icon(Icons.Default.Check, contentDescription = null) }
                } else null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoItem(memo: Memo, onClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val date = Date(memo.createdTs)
    val formattedDate = dateFormat.format(date)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (memo.pinned) {
                        Icon(
                            Icons.Default.PushPin,
                            contentDescription = "置顶",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    
                    val visibilityIcon = when(memo.visibility) {
                        "PRIVATE" -> Icons.Default.Lock
                        "PROTECTED" -> Icons.Default.Group
                        "PUBLIC" -> Icons.Default.Public
                        else -> Icons.Default.Lock
                    }
                    
                    Icon(
                        visibilityIcon,
                        contentDescription = memo.visibility,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = memo.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}