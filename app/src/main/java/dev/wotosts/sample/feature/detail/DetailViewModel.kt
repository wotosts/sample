package dev.wotosts.sample.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.wotosts.sample.domain.BookRepository
import dev.wotosts.sample.domain.model.Book
import dev.wotosts.sample.feature.base.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class DetailUiState(
    val book: Book? = null,
    val isLoading: Boolean = true
)

sealed class DetailUiEffect {
    data class OnClickedWeb(val link: String) : DetailUiEffect()
    data object OnClickedFinish : DetailUiEffect()
    data object ShowErrorToast : DetailUiEffect()
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle
) :
    BaseViewModel<DetailUiState, DetailUiEffect>(
        DetailUiState()
    ) {

    private val errorHandler = CoroutineExceptionHandler { context, throwable ->
        emitEffect(DetailUiEffect.ShowErrorToast)
    }

    init {
        loadBook(savedStateHandle.get<String>(DetailActivity.KEY_ISBN))
    }

    private fun loadBook(isbn: String?) {
        if (isbn != null) {
            viewModelScope.launch(errorHandler) {
                val book = bookRepository.getBookDetail(isbn)
                _uiState.update { it.copy(book = book) }
            }.invokeOnCompletion {
                _uiState.update { it.copy(isLoading = false) }
            }
        } else {
            emitEffect(DetailUiEffect.ShowErrorToast)
        }
    }

    fun onClickWeb() {
        uiState.value.book?.url?.let {
            emitEffect(DetailUiEffect.OnClickedWeb(it))
        }
    }

    fun onClickFinish() {
        emitEffect(DetailUiEffect.OnClickedFinish)
    }
}