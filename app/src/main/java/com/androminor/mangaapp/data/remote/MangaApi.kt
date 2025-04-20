package com.androminor.mangaapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface MangaApi {
    @GET("fetch-manga")
    suspend fun fetchManga(
        @Query("page") page: Int,
        @Query("size") size: Int = 10//can be >10
    ): MangaResponse
}