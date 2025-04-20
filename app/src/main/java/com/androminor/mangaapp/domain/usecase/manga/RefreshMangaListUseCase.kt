package com.androminor.mangaapp.domain.usecase.manga

import com.androminor.mangaapp.domain.repository.MangaRepository
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class RefreshMangaListUseCase @Inject constructor(private val mangaRepository: MangaRepository) {
suspend operator fun invoke(){
    mangaRepository.refreshMangaList()
}
}