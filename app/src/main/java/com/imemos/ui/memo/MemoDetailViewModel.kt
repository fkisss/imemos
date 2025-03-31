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

// 笔记详情状态
sealed class MemoDetailState {
    object Loading : MemoDetailState()
    data class Success(val memo: Memo) : MemoDetailState()
    data class Error(val message: String) : MemoDetailState()
}

@HiltViewModel
class MemoDetailViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : ViewModel() {
    
    private val _memoState = MutableStateFlow<MemoDetailState>(MemoDetailState.Loading)
    val memoState: StateFlow<MemoDetailState> = _memoState.asStateFlow()
    
    fun loadMemo(id: Int) {
        viewModelScope.launch {
            _memoState.value = MemoDetailState.Loading
            try {
                memoRepository.getMemo(id).collect { result ->
                    result.fold(
                        onSuccess = { memo ->
                            _memoState.value = MemoDetailState.Success(memo)
                        },
                        onFailure = { exception ->
                            _memoState.value = MemoDetailState.Error(exception.message ?: "未知错误")
                        }
                    )
                }
            } catch (e: Exception) {
                _memoState.value = MemoDetailState.Error(e.message ?: "未知错误")
            }
        }
    }
    
    fun deleteMemo(id: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                memoRepository.deleteMemo(id).collect { result ->
                    result.fold(
                        onSuccess = {
                            onSuccess()
                        },
                        onFailure = { exception ->
                            _memoState.value = MemoDetailState.Error(exception.message ?: "删除失败")
                        }
                    )
                }
            } catch (e: Exception) {
                _memoState.value = MemoDetailState.Error(e.message ?: "删除失败")
            }
        }
    }
}