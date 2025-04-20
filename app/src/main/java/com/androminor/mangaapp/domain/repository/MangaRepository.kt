package com.androminor.mangaapp.domain.repository

import androidx.paging.PagingData
import com.androminor.mangaapp.domain.model.Manga
import kotlinx.coroutines.flow.Flow

interface MangaRepository {
    fun getMangasPagingData(): Flow<PagingData<Manga>>
    suspend fun refreshMangaList()
    suspend fun getMangaById(id: String): Manga?// removal for later
    fun observeMangaById(id: String): Flow<Manga?>
    suspend fun shouldRefreshData():Boolean //observe update from room db dao query
}