package dev.wotosts.sample.feature.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.wotosts.sample.domain.BookRepository
import dev.wotosts.sample.domain.model.PAGE_SIZE
import dev.wotosts.sample.domain.model.SimpleBook
import dev.wotosts.sample.feature.base.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 화면 상태
 */
enum class SearchState {
    RESULT, EMPTY
}

data class SearchUiState(
    val keyword: String = "",
    val books: List<SimpleBook> = listOf(),
    val loading: Boolean = false,
    val searchState: SearchState = SearchState.RESULT,
    val itemViewType: ItemViewType = ItemViewType.LINEAR
)

sealed class SearchUiEffect {
    data object ShowErrorToast : SearchUiEffect()
    data object OnSearched : SearchUiEffect()
    data class ChangedItemViewType(val viewType: ItemViewType) : SearchUiEffect()
}

data class Page(
    val keyword: String = "",
    val page: Int = 1,
    val isLastPage: Boolean = false
)


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : BaseViewModel<SearchUiState, SearchUiEffect>(SearchUiState()) {
    var currentPage = Page()
        private set

    private val errorHandler = CoroutineExceptionHandler { context, throwable ->
        emitEffect(SearchUiEffect.ShowErrorToast)
    }

    init {
        getNewBooks()
    }

    fun onChangedSearchKeyword(keyword: String) {
        _uiState.updateAndGet {
            it.copy(
                keyword = keyword
            )
        }
    }

    fun loadMore() {
        searchInternal { currentPage ->
            currentPage.copy(keyword = currentPage.keyword, page = currentPage.page + 1)
        }
    }

    fun search(keyword: String) {
        if (uiState.value.loading) return

        _uiState.update { it.copy(books = listOf()) }
        if (keyword.isEmpty()) {
            getNewBooks()
        } else {
            searchInternal {
                Page(
                    keyword = keyword,
                    isLastPage = false,
                    page = 1
                )
            }
        }
    }

    private fun searchInternal(getPage: (Page) -> Page) {
        viewModelScope.launch(errorHandler) {
            val page = getPage(currentPage)
            if (page.keyword.isEmpty()) return@launch

            emitEffect(SearchUiEffect.OnSearched)
            _uiState.update {
                it.copy(
                    keyword = page.keyword,
                    loading = true
                )
            }

            val result = bookRepository.search(page.keyword, page.page)
            currentPage = page.copy(isLastPage = result.size < PAGE_SIZE)
            Log.d("page", "$page")

            _uiState.update {
                val newProduct = it.books.plus(result)
                it.copy(
                    books = newProduct,
                    loading = false,
                    searchState = if (newProduct.isEmpty()) SearchState.EMPTY else SearchState.RESULT
                )
            }
        }.invokeOnCompletion {
            updateLoading(false)
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        _uiState.update { it.copy(loading = isLoading) }
    }

    private fun getNewBooks() {
        viewModelScope.launch(errorHandler) {
            updateLoading(true)
            val books = bookRepository.getNewBooks()
            _uiState.update {
                it.copy(books = books)
            }
        }.invokeOnCompletion {
            updateLoading(false)
        }
    }

    fun onToggleItemViewType(isLinear: Boolean) {
        val viewType = if (isLinear) ItemViewType.LINEAR else ItemViewType.GRID
        _uiState.update {
            it.copy(itemViewType = viewType)
        }
        emitEffect(SearchUiEffect.ChangedItemViewType(viewType))
    }
}