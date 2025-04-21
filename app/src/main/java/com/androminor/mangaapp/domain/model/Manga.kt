package com.androminor.mangaapp.domain.model

data class Manga(
    val id:String,
    val title: String,
    val subTitle: String,
    val status: String,
    val thumb: String,
    val summary: String,
    val authors: List<String>,
    val genres: List<String>,
    val isNsfw: Boolean,
    val type: String,
    val totalChapters: Int,
    val createdAt: Long,
    val updatedAt: Long
)
