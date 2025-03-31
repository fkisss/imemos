package com.imemos.data.repository

import com.imemos.api.ApiClient
import com.imemos.data.local.TokenManager
import com.imemos.data.model.AuthRequest
import com.imemos.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepository(private val apiClient: ApiClient, private val tokenManager: TokenManager) {
    
    // 用户登录
    fun signIn(username: String, password: String) = flow {
        try {
            val authRequest = AuthRequest(username, password)
            val response = apiClient.getMemosApi().signIn(authRequest)
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    // 保存令牌和用户名
                    tokenManager.saveToken(authResponse.token)
                    tokenManager.saveUsername(username)
                    emit(Result.success(authResponse.user))
                }
            } else {
                emit(Result.failure(Exception("登录失败: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    // 用户登出
    fun signOut() = flow<Result<Unit>> {
        try {
            val response = apiClient.getMemosApi().signOut()
            // 无论API响应如何，都清除本地令牌
            tokenManager.clearToken()
            if (response.isSuccessful) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("登出失败: ${response.code()}")))
            }
        } catch (e: Exception) {
            // 即使API调用失败，也清除本地令牌
            tokenManager.clearToken()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    // 获取当前用户信息
    fun getCurrentUser() = flow {
        try {
            val response = apiClient.getMemosApi().getCurrentUser()
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.success(it)) }
            } else {
                emit(Result.failure(Exception("获取用户信息失败: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    // 获取所有用户
    fun getUsers() = flow {
        try {
            val response = apiClient.getMemosApi().getUsers()
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.success(it)) }
            } else {
                emit(Result.failure(Exception("获取用户列表失败: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    // 检查是否已登录
    fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }
}