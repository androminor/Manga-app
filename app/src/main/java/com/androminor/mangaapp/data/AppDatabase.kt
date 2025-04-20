package com.androminor.mangaapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androminor.mangaapp.data.local.convertors.Converters
import com.androminor.mangaapp.data.local.dao.MangaDao
import com.androminor.mangaapp.data.local.dao.UserDao
import com.androminor.mangaapp.data.local.entity.MangaEntity
import com.androminor.mangaapp.data.local.entity.UserEntity

/**
 * Created by Varun Singh
 */
@Database(entities = [UserEntity::class,MangaEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun mangaDao():MangaDao


}