package dev.wotosts.sample.feature.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import dev.wotosts.sample.BR

class BaseViewHolder(
    parent: ViewGroup,
    layoutId: Int
) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            layoutId,
            parent,
            false
        )
    ) {

    private val binding: ViewDataBinding? = DataBindingUtil.bind(itemView)
    fun bind(item: Any?, variables: Map<Int, Any> = mapOf()) {
        item?.let {
            binding?.setVariable(BR.item, item)
        }
        variables.forEach {
            binding?.setVariable(it.key, it.value)
        }
        binding?.executePendingBindings()
    }
}