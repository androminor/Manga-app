package com.androminor.mangaapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.androminor.mangaapp.data.local.dao.MangaDao
import com.androminor.mangaapp.data.local.dao.UserDao
import com.androminor.mangaapp.data.local.entity.MangaEntity
import com.androminor.mangaapp.data.local.entity.UserEntity

/**
 * Created by Varun Singh
 */
@Database(entities = [UserEntity::class,MangaEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun mangaDao():MangaDao


}