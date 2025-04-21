package com.androminor.mangaapp.di

import android.content.Context
import androidx.room.Room
import com.androminor.mangaapp.data.AppDatabase
import com.androminor.mangaapp.data.local.dao.MangaDao
import com.androminor.mangaapp.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Varun Singh
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "Manga database"
        )
            .fallbackToDestructiveMigration(false)  // This will clear DB if version changes without migration
            .build()
    }

    @Provides
    @Singleton
    fun providesUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideMangaDao(appDatabase: AppDatabase): MangaDao {
        return appDatabase.mangaDao()
    }
}