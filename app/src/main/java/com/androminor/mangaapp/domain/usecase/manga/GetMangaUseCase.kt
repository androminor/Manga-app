package com.androminor.mangaapp.domain.usecase.manga

import com.androminor.mangaapp.domain.model.Manga
import com.androminor.mangaapp.domain.repository.MangaRepository
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class GetMangaUseCase @Inject constructor(private val mangaRepository: MangaRepository) {
    suspend operator fun invoke(mangaID:String): Manga?{
        return mangaRepository.getMangaById(mangaID)
    }

}