package com.androminor.mangaapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.androminor.mangaapp.data.local.entity.MangaEntity
import com.androminor.mangaapp.data.source.LocalMangaDataSource
import com.androminor.mangaapp.data.source.RemoteMangaDataSource
import com.androminor.mangaapp.domain.model.Manga
import com.androminor.mangaapp.domain.repository.MangaRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class MangaRepositoryImpl @Inject constructor(
    private val localMangaDataSource: LocalMangaDataSource,
    private val remoteMangaDataSource: RemoteMangaDataSource
) : MangaRepository {
    override fun getMangasPagingData(): Flow<PagingData<Manga>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { localMangaDataSource.getAllMangasPagingSource() }

        ).flow.map { pagingData ->
            pagingData.map { mangaEntity -> mangaEntity.toDomain() }
        }
    }


    override suspend fun refreshMangaList() {
        try {
            val lastLocalPage = localMangaDataSource.getLastPage() ?: 0
            var page = lastLocalPage + 1
            while (true) {
                val remoteManga = remoteMangaDataSource.fetchManga(page)

                if (remoteManga.isEmpty()) break

                //if found any manga then insert
                if (remoteManga.isNotEmpty()) {
                    localMangaDataSource.insertMangas(remoteManga)
                }
                page++
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override suspend fun getMangaById(id: String): Manga? {
        return localMangaDataSource.getMangaById(id).map { it?.toDomain() }.firstOrNull()
    }

    override fun observeMangaById(id: String): Flow<Manga?> {
        return localMangaDataSource.getMangaById(id).map { it?.toDomain() }
    }

    override suspend fun shouldRefreshData(): Boolean {
        //if room is empty refresh
        val count = localMangaDataSource.getMangasByCount()
        return count == 0
    }

}

// MangaEntity -> Manga (Domain model)
fun MangaEntity.toDomain(): Manga {
    val gson = Gson()
    return Manga(
        id = id,
        title = title,
        subTitle = subTitle,
        status = status,
        thumbnailUrl = thumbnailUrl,
        summary = summary,
        authors = gson.fromJson(authors, Array<String>::class.java).toList(),
        genres = gson.fromJson(genres, Array<String>::class.java).toList(),
        isNsfw = isNsfw,
        type = type,
        totalChapters = totalChapters,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}