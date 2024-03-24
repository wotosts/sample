package dev.wotosts.sample.domain

import dev.wotosts.sample.domain.model.Book
import dev.wotosts.sample.domain.model.SimpleBook

interface BookRepository {
    suspend fun search(keyword: String, page: Int): List<SimpleBook>

    suspend fun getNewBooks(): List<SimpleBook>

    suspend fun getBookDetail(isbn: String): Book
}