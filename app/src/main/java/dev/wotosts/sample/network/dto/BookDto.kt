package dev.wotosts.sample.network.dto

import dev.wotosts.sample.domain.model.Book
import dev.wotosts.sample.domain.model.SimpleBook

/**
 * @property error 무슨 값인지 모르겠음
 */
data class BookDto(
    val error: String,
    val title: String,
    val subtitle: String,
    val author: String?,
    val publisher: String,
    val isbn10: String,
    val isbn13: String,
    val page: String?,
    val year: String?,
    val rating: String,
    val desc: String,
    val image: String,
    val url: String,
    val price: String
)

data class SimpleBookDto(
    val title: String,
    val subtitle: String,
    val isbn13: String,
    val price: String,
    val image: String,
    val url: String
)

fun SimpleBookDto.toDomain() = SimpleBook(
    title = title,
    subtitle = subtitle,
    isbn = isbn13,
    price = price,
    image = image
)

fun BookDto.toDomain() = Book(
    title = title,
    subtitle = subtitle,
    author = author.orEmpty(),
    publisher = publisher,
    isbn10 = isbn10,
    isbn13 = isbn13,
    image = image,
    page = page?.toInt() ?: 0,
    rating = rating.toFloat(),
    year = year?.toInt() ?: 0,
    desc = desc,
    url = url,
    price = price
)
