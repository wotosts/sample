package dev.wotosts.sample.feature.search

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.wotosts.sample.R
import dev.wotosts.sample.databinding.ActivitySearchBinding
import dev.wotosts.sample.domain.model.SimpleBook
import dev.wotosts.sample.feature.base.BaseActivity
import dev.wotosts.sample.feature.clearFocusAndHideKeyboard
import dev.wotosts.sample.feature.comm.PaginationScrollListener
import dev.wotosts.sample.feature.detail.DetailActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>(
    layoutId = R.layout.activity_search
) {
    private val viewModel by viewModels<SearchViewModel>()
    private val bookAdapter by lazy {
        BookAdapter(
            onClick = ::showDetail,
            currentViewType = { viewModel.uiState.value.itemViewType }
        )
    }
    private lateinit var scrollListener: PaginationScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind {
            vm = viewModel
            rvResult.adapter = bookAdapter
        }

        scrollListener = object :
            PaginationScrollListener(binding.rvResult.layoutManager as? LinearLayoutManager) {
            override fun isLastPage(): Boolean = viewModel.currentPage.isLastPage

            override fun isLoading(): Boolean = viewModel.uiState.value.loading

            override fun loadMoreItems() {
                viewModel.loadMore()
            }
        }

        setupListeners()
        collectEffect()
    }

    private fun collectEffect() {
        viewModel.effect.onEach { effect ->
            when (effect) {
                is SearchUiEffect.ShowErrorToast -> Toast.makeText(
                    this,
                    "오류가 발생했어요.",
                    Toast.LENGTH_SHORT
                ).show()

                is SearchUiEffect.OnSearched -> with(binding) {
                    if (etSearch.hasFocus()) {
                        clearFocusAndHideKeyboard(etSearch.rootView)
                    }
                }

                is SearchUiEffect.ChangedItemViewType -> {
                    (binding.rvResult.layoutManager as? GridLayoutManager)?.spanCount =
                        effect.viewType.spanCount
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun setupListeners() {
        with(binding) {
            rvResult.addOnScrollListener(scrollListener)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    clearFocusAndHideKeyboard(v)
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }

    private fun showDetail(item: SimpleBook) {
        val intent = Intent(this, DetailActivity::class.java)
            .run {
                putExtra(DetailActivity.KEY_ISBN, item.isbn)
            }

        startActivity(intent)
    }
}