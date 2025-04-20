import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.androminor.mangaapp.domain.model.Manga
import com.androminor.mangaapp.domain.usecase.manga.CheckMangaDataStatsUseCase
import com.androminor.mangaapp.domain.usecase.manga.GetMangasPaginationDataUseCase
import com.androminor.mangaapp.domain.usecase.manga.RefreshMangaListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Varun Singh
 */
@HiltViewModel
class MangaViewModel @Inject constructor(
    private val checkMangaDataStatsUseCase: CheckMangaDataStatsUseCase,
    private val getMangasPaginationDataUseCase: GetMangasPaginationDataUseCase,
    private val refreshMangaListUseCase: RefreshMangaListUseCase,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val mangasPaginationData: Flow<PagingData<Manga>> =
        getMangasPaginationDataUseCase().cachedIn(viewModelScope)

    init {
        checkAndRefreshData()
        setNetworkObserver()
    }

    private fun checkAndRefreshData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                if (checkMangaDataStatsUseCase()) {
                    refreshMangaListUseCase() //calling invoke()
                }
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun setNetworkObserver() {
        viewModelScope.launch {
            connectivityObserver.observeNetworkStatus()
                .collect { hasInternet ->
                    if (hasInternet) {
                        refresh()
                    }
                }

        }
    }


    private fun refresh() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                refreshMangaListUseCase()
                _errorMessage.value = null

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

}

