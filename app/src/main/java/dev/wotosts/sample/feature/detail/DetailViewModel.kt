package dev.wotosts.sample.feature.detail

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.wotosts.sample.domain.BookRepository
import dev.wotosts.sample.domain.model.Book
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@Parcelize
data class DetailUiState(
    val book: Book? = null,
    val isLoading: Boolean = true
) : Parcelable

sealed class DetailUiEffect {
    data class OnClickedWeb(val link: String) : DetailUiEffect()
    data object OnClickedFinish : DetailUiEffect()
    data object ShowErrorToast : DetailUiEffect()
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    savedStateHandle: SavedStateHandle,
) : ContainerHost<DetailUiState, DetailUiEffect>,
    ViewModel() {

    override val container =
        container<DetailUiState, DetailUiEffect>(
            initialState = DetailUiState(),
            savedStateHandle = savedStateHandle
        )

    private val errorHandler = CoroutineExceptionHandler { context, throwable ->
        intent {
            postSideEffect(DetailUiEffect.ShowErrorToast)
        }
    }

    init {
        loadBook(savedStateHandle.get<String>(DetailActivity.KEY_ISBN))
    }

    private fun loadBook(isbn: String?) {
        if (isbn != null) {
            viewModelScope.launch(errorHandler) {
                val book = bookRepository.getBookDetail(isbn)
                intent { reduce { state.copy(book = book) } }
            }.invokeOnCompletion {
                intent { reduce { state.copy(isLoading = false) } }
            }
        } else {
            intent { postSideEffect(DetailUiEffect.ShowErrorToast) }
        }
    }

    fun onClickWeb() {
        intent {
            state.book?.url?.let {
                postSideEffect(DetailUiEffect.OnClickedWeb(it))
            }
        }
    }

    fun onClickFinish() {
        intent {
            postSideEffect(DetailUiEffect.OnClickedFinish)
        }
    }
}