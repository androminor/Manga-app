package com.androminor.mangaapp.di

import com.androminor.mangaapp.data.repository.MangaRepositoryImpl
import com.androminor.mangaapp.data.repository.UserRepositoryImpl
import com.androminor.mangaapp.domain.repository.MangaRepository
import com.androminor.mangaapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Varun Singh
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindsMangaRepository(
        mangaRepositoryImpl: MangaRepositoryImpl
    ):MangaRepository
}