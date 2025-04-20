package com.androminor.mangaapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androminor.mangaapp.data.local.entity.MangaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MangaDao {

/*    @Query("SELECT * FROM mangas WHERE page = :page ORDER BY createdAt DESC")
    fun getMangasByPage(page: Int): Flow<List<MangaEntity>>*/

   // Detail screen
    @Query("SELECT * FROM mangas WHERE id =:id")
    fun getMangaById(id:String):Flow<MangaEntity?>

    //used by paging3 to paginate from room
    @Query("SELECT * FROM mangas ORDER BY updatedAt DESC")
    fun getAllMangasPagingSource():PagingSource<Int,MangaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mangas:List<MangaEntity>)

    //calculate next page and check sync stats
    @Query("SELECT MAX(page) FROM mangas")
    suspend fun getLastPage():Int?

    @Query("Select COUNT(*) FROM mangas")
    suspend fun getMangaCount():Int

}