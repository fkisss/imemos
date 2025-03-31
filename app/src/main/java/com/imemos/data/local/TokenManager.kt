package com.imemos.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore("imemos_prefs")

class TokenManager(private val context: Context) {
    
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val SERVER_URL_KEY = stringPreferencesKey("server_url")
        private val USERNAME_KEY = stringPreferencesKey("username")
    }
    
    // 获取令牌
    fun getToken(): String {
        var token = ""
        runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[TOKEN_KEY] ?: ""
            }.collect {
                token = it
            }
        }
        return token
    }
    
    // 保存令牌
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }
    
    // 清除令牌
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
    
    // 获取服务器URL
    fun getServerUrl(): String {
        var url = ""
        runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[SERVER_URL_KEY] ?: ""
            }.collect {
                url = it
            }
        }
        return url
    }
    
    // 保存服务器URL
    suspend fun saveServerUrl(serverUrl: String) {
        context.dataStore.edit { preferences ->
            preferences[SERVER_URL_KEY] = serverUrl
        }
    }
    
    // 获取用户名
    fun getUsername(): String {
        var username = ""
        runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[USERNAME_KEY] ?: ""
            }.collect {
                username = it
            }
        }
        return username
    }
    
    // 保存用户名
    suspend fun saveUsername(username: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
        }
    }
    
    // 清除所有数据
    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
    
    // 检查是否已登录
    fun isLoggedIn(): Boolean {
        return getToken().isNotEmpty() && getServerUrl().isNotEmpty()
    }