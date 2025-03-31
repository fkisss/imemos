package com.imemos.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imemos.api.ApiClient
import com.imemos.data.local.TokenManager
import com.imemos.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 登录界面状态
data class LoginUiState(
    val serverUrl: String = "",
    val serverUrlError: String? = null,
    val username: String = "",
    val usernameError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val canLogin: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val apiClient: ApiClient,
    private val tokenManager: TokenManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    
    init {
        // 如果已经有保存的服务器地址，自动填充
        val savedServerUrl = tokenManager.getServerUrl()
        if (savedServerUrl.isNotEmpty()) {
            updateServerUrl(savedServerUrl)
        }
        
        // 如果已经有保存的用户名，自动填充
        val savedUsername = tokenManager.getUsername()
        if (savedUsername.isNotEmpty()) {
            updateUsername(savedUsername)
        }
    }
    
    // 更新服务器地址
    fun updateServerUrl(serverUrl: String) {
        val error = validateServerUrl(serverUrl)
        _uiState.value = _uiState.value.copy(
            serverUrl = serverUrl,
            serverUrlError = error,
            canLogin = canLogin(serverUrl, _uiState.value.username, _uiState.value.password, error)
        )
    }
    
    // 更新用户名
    fun updateUsername(username: String) {
        val error = validateUsername(username)
        _uiState.value = _uiState.value.copy(
            username = username,
            usernameError = error,
            canLogin = canLogin(_uiState.value.serverUrl, username, _uiState.value.password, error)
        )
    }
    
    // 更新密码
    fun updatePassword(password: String) {
        val error = validatePassword(password)
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = error,
            canLogin = canLogin(_uiState.value.serverUrl, _uiState.value.username, password, error)
        )
    }
    
    // 验证服务器地址
    private fun validateServerUrl(serverUrl: String): String? {
        return when {
            serverUrl.isBlank() -> "服务器地址不能为空"
            !serverUrl.startsWith("http://") && !serverUrl.startsWith("https://") -> "服务器地址必须以http://或https://开头"
            else -> null
        }
    }
    
    // 验证用户名
    private fun validateUsername(username: String): String? {
        return when {
            username.isBlank() -> "用户名不能为空"
            else -> null
        }
    }
    
    // 验证密码
    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "密码不能为空"
            else -> null
        }
    }
    
    // 检查是否可以登录
    private fun canLogin(serverUrl: String, username: String, password: String, error: String? = null): Boolean {
        return serverUrl.isNotBlank() && username.isNotBlank() && password.isNotBlank() && error == null
            && _uiState.value.serverUrlError == null && _uiState.value.usernameError == null && _uiState.value.passwordError == null
    }
    
    // 登录
    fun login(onSuccess: () -> Unit) {
        val serverUrl = _uiState.value.serverUrl
        val username = _uiState.value.username
        val password = _uiState.value.password
        
        if (!canLogin(serverUrl, username, password)) return
        
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            error = null
        )
        
        // 更新API客户端的基础URL
        apiClient.updateBaseUrl(serverUrl)
        
        viewModelScope.launch {
            try {
                userRepository.signIn(username, password).collect { result ->
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
                                error = exception.message ?: "登录失败"
                            )
                        }
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "登录失败"
                )
            }
        }
    }
}