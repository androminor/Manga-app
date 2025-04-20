package com.androminor.mangaapp.domain.usecase.manga

import androidx.paging.PagingData
import com.androminor.mangaapp.domain.model.Manga
import com.androminor.mangaapp.domain.repository.MangaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
class GetMangasPaginationDataUseCase @Inject constructor(
    private val mangaRepository: MangaRepository
) {
operator fun invoke(): Flow<PagingData<Manga>> {
    return mangaRepository.getMangasPagingData()
}
}