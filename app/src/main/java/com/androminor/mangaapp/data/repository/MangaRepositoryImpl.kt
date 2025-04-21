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
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MangaRepositoryImpl @Inject constructor(
    private val localMangaDataSource: LocalMangaDataSource,
    private val remoteMangaDataSource: RemoteMangaDataSource
) : MangaRepository {
    override fun getMangasPagingData(): Flow<PagingData<Manga>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,         // Load 15 items per page
                prefetchDistance = 5,  // Start loading when 10 items away from the end
                initialLoadSize = 10,   // Load 30 items initially
                enablePlaceholders = false,
                maxSize = 50          // Limit cached items to prevent memory issues
            ),
            pagingSourceFactory = { localMangaDataSource.getAllMangasPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { mangaEntity -> mangaEntity.toDomain() }
        }
    }

    override suspend fun refreshMangaList() {
        try {
            val lastLocalPage = localMangaDataSource.getLastPage()
            var page = lastLocalPage + 1
            while (true) {
                try {
                    val remoteMangaEntities = remoteMangaDataSource.fetchManga(page).map {
                        // Normalize authors and genres immediately after fetching
                        it.copy(
                            authors = normalizeToJsonArrayString(it.authors),
                            genres = normalizeToJsonArrayString(it.genres)
                        )
                    }

                    if (remoteMangaEntities.isEmpty()) break

                    if (remoteMangaEntities.isNotEmpty()) {
                        localMangaDataSource.insertMangas(remoteMangaEntities)
                    }
                    page++
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Log the error and potentially decide whether to continue fetching or stop
                    // For now, let's break the loop on any error during fetching a page
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    private fun normalizeToJsonArrayString(data: Any?): String {
        return when (data) {
            is String -> {
                if (data.startsWith("[") && data.endsWith("]")) {
                    data
                } else if (data.isNotEmpty()) {
                    "[\"$data\"]"
                } else {
                    "[]"
                }
            }
            is List<*> -> {
                val gson = Gson()
                gson.toJson(data)
            }
            else -> "[]"
        }
    }

    override suspend fun getMangaById(id: String): Manga? {
        return localMangaDataSource.getMangaById(id).map { it?.toDomain() }.firstOrNull()
    }

    override fun observeMangaById(id: String): Flow<Manga?> {
        return localMangaDataSource.getMangaById(id).map { it?.toDomain() }
    }

    override suspend fun shouldRefreshData(): Boolean {
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
        thumb = thumb,
        summary = summary,
        authors = safeParseStringList(authors, gson),
        genres = safeParseStringList(genres, gson),
        isNsfw = isNsfw,
        type = type,
        totalChapters = totalChapters,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}

fun safeParseStringList(jsonString: String?, gson: Gson): List<String> {
    if (jsonString.isNullOrEmpty()) {
        return emptyList()
    }
    return try {
        val type = object : TypeToken<List<String>>() {}.type
        gson.fromJson(jsonString, type) ?: emptyList()
    } catch (e: JsonSyntaxException) {
        try {
            listOf(gson.fromJson(jsonString, String::class.java))
        } catch (e2: JsonSyntaxException) {
            emptyList()
        }
    }
}