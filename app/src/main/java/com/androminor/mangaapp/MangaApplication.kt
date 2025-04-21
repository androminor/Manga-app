package com.androminor.mangaapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Varun Singh
 */
@HiltAndroidApp
class MangaApplication : Application() {
/*    override fun onCreate() {
        super.onCreate()

        // Initialize default ImageLoader for Coil
        val imageLoader = ImageLoader.Builder(this)
            .crossfade(true)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25) // Use 25% of app memory for caching
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50 * 1024 * 1024) // 50MB cache
                    .build()
            }
            .build()

        Coil.setImageLoader(imageLoader)
    }*/
}