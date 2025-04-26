package com.androminor.mangaapp.di

import com.androminor.mangaapp.domain.repository.MangaRepository
import com.androminor.mangaapp.domain.repository.UserRepository
import com.androminor.mangaapp.domain.usecase.auth.CheckUserLoginStatusUseCase
import com.androminor.mangaapp.domain.usecase.auth.CreateAccountUseCase
import com.androminor.mangaapp.domain.usecase.auth.GetLoggedInUserUseCase
import com.androminor.mangaapp.domain.usecase.auth.LogOutUseCase
import com.androminor.mangaapp.domain.usecase.auth.SaveLoggedUserUseCase
import com.androminor.mangaapp.domain.usecase.auth.SignInUseCase
import com.androminor.mangaapp.domain.usecase.manga.CheckMangaDataStatsUseCase
import com.androminor.mangaapp.domain.usecase.manga.GetMangaUseCase
import com.androminor.mangaapp.domain.usecase.manga.GetMangasPaginationDataUseCase
import com.androminor.mangaapp.domain.usecase.manga.ObserveDetailByIdUseCase
import com.androminor.mangaapp.domain.usecase.manga.RefreshMangaListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Varun Singh
 */

/*Auth usecases(Signin)*/
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSignInUseCase(repository: UserRepository): SignInUseCase {
        return SignInUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateAccountUseCase(repository: UserRepository): CreateAccountUseCase {
        return CreateAccountUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCheckUserLoginStatusUseCase(repository: UserRepository): CheckUserLoginStatusUseCase {
        return CheckUserLoginStatusUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetLoggedInUserUseCase(repository: UserRepository): GetLoggedInUserUseCase {
        return GetLoggedInUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveLoggedUserUseCase(repository: UserRepository): SaveLoggedUserUseCase {
        return SaveLoggedUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLogOutUseCase(repository: UserRepository): LogOutUseCase {
        return LogOutUseCase(repository)
    }

    /*Manga usecases*/

    @Provides
    @Singleton
    fun provideGetMangasPaginationDataUseCase(mangaRepository: MangaRepository): GetMangasPaginationDataUseCase {
        return GetMangasPaginationDataUseCase(mangaRepository)
    }

    @Provides
    @Singleton
    fun provideCheckMangaDataStatsUseCase(mangaRepository: MangaRepository): CheckMangaDataStatsUseCase {
        return CheckMangaDataStatsUseCase(mangaRepository)
    }

    @Provides
    @Singleton
    fun provideGetMangaByIdUseCase(mangaRepository: MangaRepository): ObserveDetailByIdUseCase {
        return ObserveDetailByIdUseCase(mangaRepository)
    }

    @Provides
    @Singleton
    fun provideGetMangaDetailUseCase(mangaRepository: MangaRepository): GetMangaUseCase {
        return GetMangaUseCase(mangaRepository)
    }
    @Provides
    @Singleton
    fun provideRefreshMangaListUseCase(mangaRepository: MangaRepository): RefreshMangaListUseCase {
        return RefreshMangaListUseCase(mangaRepository)
    }

}