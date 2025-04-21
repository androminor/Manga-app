package com.androminor.mangaapp.network.util

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Varun Singh
 */
class RapidApiInterceptor(private val apikey: String, private val apiHost:String):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()
            .addHeader("X-RapidAPI-Key",apikey)
            .addHeader("X-RapidAPI-Host", apiHost)
            .build()
        return chain.proceed(request)

    }


}