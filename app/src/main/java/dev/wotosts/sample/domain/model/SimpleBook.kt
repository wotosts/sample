package dev.wotosts.sample.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SimpleBook(
    val title: String,
    val subtitle: String,
    val image: String?,
    val isbn: String,
    val price: String
) : Parcelable

const val PAGE_SIZE = 10