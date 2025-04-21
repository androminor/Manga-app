package com.androminor.mangaapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface MangaApi {
    @GET("manga/fetch")
    suspend fun fetchManga(
        @Query("page") page: Int,
        @Query("size") size: Int = 5): MangaResponse
}