package com.imemos.api

import com.imemos.data.model.AuthRequest
import com.imemos.data.model.AuthResponse
import com.imemos.data.model.Memo
import com.imemos.data.model.MemoCreate
import com.imemos.data.model.MemoUpdate
import com.imemos.data.model.User
import retrofit2.Response
import retrofit2.http.*

interface MemosApi {
    
    // 认证相关
    @POST("api/v1/auth/signin")
    suspend fun signIn(@Body request: AuthRequest): Response<AuthResponse>
    
    @POST("api/v1/auth/signout")
    suspend fun signOut(): Response<Unit>
    
    // 用户相关
    @GET("api/v1/user/me")
    suspend fun getCurrentUser(): Response<User>
    
    @GET("api/v1/users")
    suspend fun getUsers(): Response<List<User>>
    
    // 笔记相关
    @GET("api/v1/memos")
    suspend fun getMemos(@Query("creatorId") creatorId: Int? = null,
                         @Query("rowStatus") rowStatus: String = "NORMAL",
                         @Query("visibility") visibility: String? = null): Response<List<Memo>>
    
    @POST("api/v1/memos")
    suspend fun createMemo(@Body memo: MemoCreate): Response<Memo>
    
    @GET("api/v1/memos/{id}")
    suspend fun getMemo(@Path("id") id: Int): Response<Memo>
    
    @PATCH("api/v1/memos/{id}")
    suspend fun updateMemo(@Path("id") id: Int, @Body memo: MemoUpdate): Response<Memo>
    
    @DELETE("api/v1/memos/{id}")
    suspend fun deleteMemo(@Path("id") id: Int): Response<Unit>
}