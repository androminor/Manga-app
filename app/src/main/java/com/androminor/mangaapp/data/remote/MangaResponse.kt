package com.androminor.mangaapp.data.remote

/**
 * Created by Varun Singh
 */
class MangaResponse(
    val currentPage: Int,
    val totalPages:Int,
    val data: List<MangaDto>
)