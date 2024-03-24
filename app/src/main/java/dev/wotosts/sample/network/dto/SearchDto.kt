package dev.wotosts.sample.network.dto

data class SearchDto(
    val total: String,
    val page: Int,
    val books: List<SimpleBookDto>
)