package com.imemos.ui.memo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imemos.data.model.Memo
import com.imemos.data.repository.MemoRepository
import com.imemos.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 笔记列表状态
sealed class MemoListState {
    object Loading : MemoListState()
    data class Success(val memos: List<Memo>) : MemoListState()
    data class Error(val message: String) : MemoListState()
}

@HiltViewModel
class MemoListViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _memoListState = MutableStateFlow<MemoListState>(MemoListState.Loading)
    val memoListState: StateFlow<MemoListState> = _memoListState.asStateFlow()
    
    private val _currentVisibilityFilter = MutableStateFlow<String?>(null)
    val currentVisibilityFilter: StateFlow<String?> = _currentVisibilityFilter.asStateFlow()
    
    init {
        loadMemos()
    }
    
    fun loadMemos() {
        viewModelScope.launch {
            _memoListState.value = MemoListState.Loading
            try {
                memoRepository.getMemos(visibility = _currentVisibilityFilter.value).collect { result ->
                    result.fold(
                        onSuccess = { memos ->
                            _memoListState.value = MemoListState.Success(memos)
                        },
                        onFailure = { exception ->
                            _memoListState.value = MemoListState.Error(exception.message ?: "未知错误")
                        }
                    )
                }
            } catch (e: Exception) {
                _memoListState.value = MemoListState.Error(e.message ?: "未知错误")
            }
        }
    }
    
    fun setVisibilityFilter(visibility: String?) {
        if (_currentVisibilityFilter.value != visibility) {
            _currentVisibilityFilter.value = visibility
            loadMemos()
        }
    }
}