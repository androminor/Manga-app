package com.androminor.mangaapp.domain.usecase.manga

import com.androminor.mangaapp.domain.repository.MangaRepository

/**
 * Created by Varun Singh
 */
//for refreshing room DB case
class CheckMangaDataStatsUseCase(private val repository: MangaRepository) {
    suspend operator fun invoke():Boolean{
    return repository.shouldRefreshData()
    }

}