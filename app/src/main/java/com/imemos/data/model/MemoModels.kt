package com.imemos.data.model

import com.google.gson.annotations.SerializedName

data class Memo(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("uid")
    val uid: String,
    
    @SerializedName("rowStatus")
    val rowStatus: String,
    
    @SerializedName("creatorId")
    val creatorId: Int,
    
    @SerializedName("createdTs")
    val createdTs: Long,
    
    @SerializedName("updatedTs")
    val updatedTs: Long,
    
    @SerializedName("content")
    val content: String,
    
    @SerializedName("visibility")
    val visibility: String,
    
    @SerializedName("pinned")
    val pinned: Boolean,
    
    @SerializedName("resources")
    val resources: List<Resource>? = null,
    
    @SerializedName("relations")
    val relations: List<MemoRelation>? = null
)

data class MemoCreate(
    @SerializedName("content")
    val content: String,
    
    @SerializedName("visibility")
    val visibility: String = "PRIVATE",
    
    @SerializedName("resources")
    val resources: List<String>? = null
)

data class MemoUpdate(
    @SerializedName("content")
    val content: String? = null,
    
    @SerializedName("visibility")
    val visibility: String? = null,
    
    @SerializedName("pinned")
    val pinned: Boolean? = null,
    
    @SerializedName("rowStatus")
    val rowStatus: String? = null
)

data class Resource(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("filename")
    val filename: String,
    
    @SerializedName("externalLink")
    val externalLink: String?,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("size")
    val size: Int,
    
    @SerializedName("createdTs")
    val createdTs: Long,
    
    @SerializedName("updatedTs")
    val updatedTs: Long
)

data class MemoRelation(
    @SerializedName("memoId")
    val memoId: Int,
    
    @SerializedName("relatedMemoId")
    val relatedMemoId: Int,
    
    @SerializedName("type")
    val type: String
)