package com.androminor.mangaapp.data.source

import com.androminor.mangaapp.data.local.entity.MangaEntity
import com.androminor.mangaapp.data.remote.MangaApi
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class RemoteMangaDataSource @Inject constructor(private val apiService:MangaApi) {
suspend fun fetchManga(page:Int):List<MangaEntity>{
    return apiService.fetchManga(page).data.map { it.toEntity(page) }
}
}