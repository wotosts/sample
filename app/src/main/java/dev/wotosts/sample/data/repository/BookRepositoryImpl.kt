package dev.wotosts.sample.data.repository

import android.util.Log
import dev.wotosts.sample.domain.BookRepository
import dev.wotosts.sample.domain.model.Book
import dev.wotosts.sample.domain.model.SimpleBook
import dev.wotosts.sample.network.BookApi
import dev.wotosts.sample.network.dto.toDomain
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val bookApi: BookApi
) : BookRepository {
    override suspend fun search(keyword: String, page: Int): List<SimpleBook> =
        bookApi.search(keyword, page).books.map { it.toDomain() }

    override suspend fun getBookDetail(isbn: String): Book = bookApi.getBookDetail(isbn).also {
        Log.d("book", "$it")
    }.toDomain()

    override suspend fun getNewBooks(): List<SimpleBook> =
        bookApi.getNewBooks().books.map { it.toDomain() }
}