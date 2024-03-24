package dev.wotosts.sample.feature.comm

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(
    private val layoutManager: LinearLayoutManager?
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (layoutManager == null) return
        if (isLoading() || isLastPage()) return

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount

        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (totalItemCount > 0 && (visibleItemCount + firstVisibleItemPosition) >= (totalItemCount - 5) &&
            firstVisibleItemPosition >= 0
        ) {
            loadMoreItems()
        }
    }

    abstract fun loadMoreItems()

    abstract fun isLoading(): Boolean

    abstract fun isLastPage(): Boolean
}