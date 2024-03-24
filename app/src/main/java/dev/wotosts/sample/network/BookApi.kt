package dev.wotosts.sample.network

import dev.wotosts.sample.network.dto.BookDto
import dev.wotosts.sample.network.dto.NewBooksDto
import dev.wotosts.sample.network.dto.SearchDto
import retrofit2.http.GET
import retrofit2.http.Path

interface BookApi {
    @GET("search/{$PATH_QUERY}/{$PATH_PAGE}")
    suspend fun search(
        @Path(PATH_QUERY) query: String,
        @Path(PATH_PAGE) page: Int
    ): SearchDto

    @GET("new")
    suspend fun getNewBooks(): NewBooksDto

    @GET("books/{$PATH_ISBN}")
    suspend fun getBookDetail(
        @Path(PATH_ISBN) isbn: String
    ): BookDto

    companion object {
        const val PATH_QUERY = "query"
        const val PATH_PAGE = "page"
        const val PATH_ISBN = "isbn13"
    }
}