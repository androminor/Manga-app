package com.androminor.mangaapp.data.remote

import com.androminor.mangaapp.data.local.entity.MangaEntity
import com.google.gson.annotations.SerializedName


data class MangaDto(
    val id: String,
    val title: String,
    @SerializedName("sub_title") val subTitle: String,
    val status: String,
    @SerializedName("thumb") val thumb: String?,
    val summary: String,
    val authors: List<String>,
    val genres: List<String>,
    val nsfw: Boolean,
    val type: String,
    @SerializedName("total_chapter") val totalChapters: Int,
    @SerializedName("create_at") val createdAt: Long,
    @SerializedName("update_at") val updatedAt: Long
) {
    fun toEntity(page: Int): MangaEntity {
        return MangaEntity(
            id = id,
            title = title,
            subTitle = subTitle,
            status = status,
            thumb = thumb?:"",
            summary = summary,
            authors = authors.joinToString(","),
            genres = genres.joinToString(","),
            isNsfw = nsfw,
            type = type,
            totalChapters = totalChapters,
            createdAt = createdAt,
            updatedAt = updatedAt,
            page = page
        )
    }
}