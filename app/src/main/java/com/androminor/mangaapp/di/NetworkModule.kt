package com.androminor.mangaapp.di

import android.content.Context
import com.androminor.mangaapp.data.remote.MangaApi
import com.androminor.mangaapp.network.util.ConnectivityObserver
import com.androminor.mangaapp.network.util.Constants
import com.androminor.mangaapp.network.util.NetworkChecker
import com.androminor.mangaapp.network.util.RapidApiInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by Varun Singh
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRapidApiInterceptor(): Interceptor {
        return RapidApiInterceptor(
            apikey = Constants.API_KEY,
            apiHost = Constants.BASE_URL
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        rapidApiInterceptor: Interceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(rapidApiInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMangaApiService(retrofit: Retrofit): MangaApi {
        return retrofit.create(MangaApi::class.java)
    }

    //Connectivity Observer
    @Provides
    fun provideConnectivityObserver(@ApplicationContext context: Context):
            ConnectivityObserver = NetworkChecker(context)

}
