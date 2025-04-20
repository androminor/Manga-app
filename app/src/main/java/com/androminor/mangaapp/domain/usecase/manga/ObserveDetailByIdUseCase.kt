package com.androminor.mangaapp.domain.usecase.manga

import com.androminor.mangaapp.domain.model.Manga
import com.androminor.mangaapp.domain.repository.MangaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class ObserveDetailByIdUseCase @Inject constructor(private val repository: MangaRepository) {
    operator fun invoke(id:String): Flow<Manga?> {
        return repository.observeMangaById(id)
    }


}