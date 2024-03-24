package dev.wotosts.sample.feature

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import dev.wotosts.sample.feature.comm.PaginationScrollListener

@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.bindRefreshing(isRefreshing: Boolean) {
    this.isRefreshing = isRefreshing
}

@BindingAdapter("items")
fun <T> RecyclerView.bindItems(items: List<T>) {
    val adapter = adapter as? ListAdapter<T, *>
    adapter?.submitList(items)
}

@BindingAdapter("imgUrl")
fun ImageView.bindImgUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .fallback(android.R.drawable.ic_menu_gallery)
        .into(this)
}

@BindingAdapter("actionListener")
fun EditText.bindActionListener(action: () -> Unit) {
    setOnEditorActionListener { v, actionId, event ->
        action()
        true
    }
}

@BindingAdapter("isVisible")
fun View.bindIsVisible(isVisible: Boolean) {
    this.isVisible = isVisible
}

@BindingAdapter(value = ["isLastPage", "isLoading", "loadMoreItems"], requireAll = true)
fun RecyclerView.bindPagingListener(
    isLastPage: () -> Boolean,
    isLoading: () -> Boolean,
    loadMoreItems: () -> Unit
) {
    object :
        PaginationScrollListener(layoutManager as? LinearLayoutManager) {
        override fun isLastPage(): Boolean = isLastPage()

        override fun isLoading(): Boolean = isLoading()

        override fun loadMoreItems() {
            loadMoreItems()
        }
    }
}