package com.androminor.mangaapp.di

import com.androminor.mangaapp.domain.repository.UserRepository
import com.androminor.mangaapp.domain.usecase.CheckUserLoginStatusUseCase
import com.androminor.mangaapp.domain.usecase.CreateAccountUseCase
import com.androminor.mangaapp.domain.usecase.GetLoggedInUserUseCase
import com.androminor.mangaapp.domain.usecase.LogOutUseCase
import com.androminor.mangaapp.domain.usecase.SaveLoggedUserUseCase
import com.androminor.mangaapp.domain.usecase.SignInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Varun Singh
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSignInUseCase(repository: UserRepository):SignInUseCase {
        return SignInUseCase(repository)
    }
    @Provides
    @Singleton
    fun provideCreateAccountUseCase(repository: UserRepository):CreateAccountUseCase{
        return CreateAccountUseCase(repository)
    }
    @Provides
    @Singleton
    fun provideCheckUserLoginStatusUseCase(repository: UserRepository):CheckUserLoginStatusUseCase {
        return CheckUserLoginStatusUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetLoggedInUserUseCase(repository: UserRepository):GetLoggedInUserUseCase {
        return GetLoggedInUserUseCase(repository)
    }
    @Provides
    @Singleton
    fun provideSaveLoggedUserUseCase(repository: UserRepository):SaveLoggedUserUseCase{
        return SaveLoggedUserUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLogOutUseCase(repository: UserRepository):LogOutUseCase {
        return LogOutUseCase(repository)
    }

}