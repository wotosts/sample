package dev.wotosts.sample.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    val title: String,
    val subtitle: String,
    val author: String,
    val publisher: String,
    val isbn10: String,
    val isbn13: String,
    val page: Int,
    val year: Int,
    val rating: Float,
    val desc: String,
    val image: String,
    val url: String,
    val price: String
): Parcelable
