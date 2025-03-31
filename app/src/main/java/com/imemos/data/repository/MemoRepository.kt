package com.imemos.data.repository

import com.imemos.api.ApiClient
import com.imemos.data.model.Memo
import com.imemos.data.model.MemoCreate
import com.imemos.data.model.MemoUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MemoRepository(private val apiClient: ApiClient) {
    
    // 获取所有笔记
    fun getMemos(creatorId: Int? = null, visibility: String? = null) = flow {
        try {
            val response = apiClient.getMemosApi().getMemos(creatorId, visibility = visibility)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.success(it)) }
            } else {
                emit(Result.failure(Exception("获取笔记失败: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    // 获取单个笔记
    fun getMemo(id: Int) = flow {
        try {
            val response = apiClient.getMemosApi().getMemo(id)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.success(it)) }
            } else {
                emit(Result.failure(Exception("获取笔记失败: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    // 创建笔记
    fun createMemo(content: String, visibility: String = "PRIVATE") = flow {
        try {
            val memoCreate = MemoCreate(content, visibility)
            val response = apiClient.getMemosApi().createMemo(memoCreate)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.success(it)) }
            } else {
                emit(Result.failure(Exception("创建笔记失败: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    // 更新笔记
    fun updateMemo(id: Int, content: String? = null, visibility: String? = null, pinned: Boolean? = null) = flow {
        try {
            val memoUpdate = MemoUpdate(content, visibility, pinned)
            val response = apiClient.getMemosApi().updateMemo(id, memoUpdate)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.success(it)) }
            } else {
                emit(Result.failure(Exception("更新笔记失败: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    // 删除笔记
    fun deleteMemo(id: Int) = flow<Result<Unit>> {
        try {
            val response = apiClient.getMemosApi().deleteMemo(id)
            if (response.isSuccessful) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("删除笔记失败: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}