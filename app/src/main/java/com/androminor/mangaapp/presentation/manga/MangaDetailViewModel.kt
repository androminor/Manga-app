import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androminor.mangaapp.domain.model.Manga
import com.androminor.mangaapp.domain.usecase.manga.ObserveDetailByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
@HiltViewModel
class MangaDetailViewModel @Inject constructor(
    observeDetailByIdUseCase: ObserveDetailByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val mangaId: String = checkNotNull(savedStateHandle["mangaId"])

    val uiState: StateFlow<MangaDetailUiState> =
        observeDetailByIdUseCase(mangaId)
            .map { manga ->
                when {
                    manga == null -> MangaDetailUiState.Empty
                    else -> MangaDetailUiState.Success(manga)
                }
            }
            .onStart { emit(MangaDetailUiState.Loading) }
            .catch { e -> emit(MangaDetailUiState.Error(e.message ?: "Unknow error")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MangaDetailUiState.Loading
            )


}

sealed class MangaDetailUiState {
    data object Loading : MangaDetailUiState()
    data class Success(val manga: Manga) : MangaDetailUiState()
    data class Error(val message: String) : MangaDetailUiState()
    data object Empty : MangaDetailUiState()
}
