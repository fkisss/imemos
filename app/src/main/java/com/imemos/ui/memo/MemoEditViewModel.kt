package com.imemos.ui.memo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imemos.data.model.Memo
import com.imemos.data.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 笔记编辑界面状态
data class MemoEditUiState(
    val content: String = "",
    val visibility: String = "PRIVATE",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPinned: Boolean = false
)

@HiltViewModel
class MemoEditViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MemoEditUiState())
    val uiState: StateFlow<MemoEditUiState> = _uiState.asStateFlow()
    
    // 加载现有笔记
    fun loadMemo(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                memoRepository.getMemo(id).collect { result ->
                    result.fold(
                        onSuccess = { memo ->
                            _uiState.value = _uiState.value.copy(
                                content = memo.content,
                                visibility = memo.visibility,
                                isPinned = memo.pinned,
                                isLoading = false,
                                error = null
                            )
                        },
                        onFailure = { exception ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = exception.message
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    // 更新内容
    fun updateContent(content: String) {
        _uiState.value = _uiState.value.copy(content = content)
    }
    
    // 更新可见性
    fun updateVisibility(visibility: String) {
        _uiState.value = _uiState.value.copy(visibility = visibility)
    }
    
    // 更新置顶状态
    fun updatePinned(pinned: Boolean) {
        _uiState.value = _uiState.value.copy(isPinned = pinned)
    }
    
    // 创建新笔记
    fun createMemo(onSuccess: () -> Unit) {
        val content = _uiState.value.content.trim()
        if (content.isBlank()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                memoRepository.createMemo(
                    content = content,
                    visibility = _uiState.value.visibility
                ).collect { result ->
                    result.fold(
                        onSuccess = {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = null
                            )
                            onSuccess()
                        },
                        onFailure = { exception ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = exception.message
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
    
    // 更新现有笔记
    fun updateMemo(id: Int, onSuccess: () -> Unit) {
        val content = _uiState.value.content.trim()
        if (content.isBlank()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                memoRepository.updateMemo(
                    id = id,
                    content = content,
                    visibility = _uiState.value.visibility,
                    pinned = _uiState.value.isPinned
                ).collect { result ->
                    result.fold(
                        onSuccess = {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = null
                            )
                            onSuccess()
                        },
                        onFailure = { exception ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = exception.message
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}