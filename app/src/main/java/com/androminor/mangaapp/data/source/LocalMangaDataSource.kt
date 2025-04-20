package com.androminor.mangaapp.data.source

import androidx.paging.PagingSource
import com.androminor.mangaapp.data.local.dao.MangaDao
import com.androminor.mangaapp.data.local.entity.MangaEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class LocalMangaDataSource @Inject constructor(private val mangaDao: MangaDao){
    suspend fun insertMangas(mangas:List<MangaEntity>){
        return mangaDao.insertAll(mangas)
    }
    fun getAllMangasPagingSource():PagingSource<Int, MangaEntity>{
        return mangaDao.getAllMangasPagingSource()
    }
    fun getMangaById(mangaId:String): Flow<MangaEntity?> {
        return mangaDao.getMangaById(mangaId)
    }
    suspend fun getMangasByCount():Int {
        return mangaDao.getMangaCount()
    }
    suspend fun getLastPage():Int {
        return mangaDao.getLastPage()?:0
    }



}