package dev.wotosts.sample.feature.search

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.wotosts.sample.R
import dev.wotosts.sample.BR
import dev.wotosts.sample.domain.model.SimpleBook
import dev.wotosts.sample.feature.base.BaseViewHolder

enum class ItemViewType(val layoutId: Int, val spanCount: Int) {
    LINEAR(R.layout.item_book_linear, 1),
    GRID(R.layout.item_book, 3)
}

class BookAdapter(
    private val onClick: (SimpleBook) -> Unit,
    private val currentViewType: () -> ItemViewType
) :
    ListAdapter<SimpleBook, BaseViewHolder>(diffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {

        return BaseViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book, mapOf(BR.onClick to View.OnClickListener { onClick(book) }))
    }

    override fun getItemViewType(position: Int): Int {
        return currentViewType().layoutId
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<SimpleBook>() {
            override fun areItemsTheSame(oldItem: SimpleBook, newItem: SimpleBook): Boolean =
                oldItem.isbn == newItem.isbn

            override fun areContentsTheSame(oldItem: SimpleBook, newItem: SimpleBook): Boolean =
                oldItem == newItem
        }
    }
}