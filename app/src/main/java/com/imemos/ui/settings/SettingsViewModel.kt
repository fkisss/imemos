package com.imemos.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imemos.data.local.TokenManager
import com.imemos.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 设置界面状态
data class SettingsUiState(
    val username: String = "",
    val serverUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadUserInfo()
    }
    
    private fun loadUserInfo() {
        _uiState.value = SettingsUiState(
            username = tokenManager.getUsername(),
            serverUrl = tokenManager.getServerUrl(),
            isLoading = false
        )
        
        viewModelScope.launch {
            try {
                userRepository.getCurrentUser().collect { result ->
                    result.fold(
                        onSuccess = { user ->
                            _uiState.value = _uiState.value.copy(
                                username = user.username,
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
    
    fun logout() {
        viewModelScope.launch {
            try {
                userRepository.signOut().collect { result ->
                    // 无论成功与否，都清除本地令牌
                    tokenManager.clearAll()
                }
            } catch (e: Exception) {
                // 即使API调用失败，也清除本地令牌
                tokenManager.clearAll()
            }
        }
    }
}