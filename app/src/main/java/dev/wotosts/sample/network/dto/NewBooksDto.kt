package dev.wotosts.sample.network.dto

data class NewBooksDto(
    val total: String,
    val books: List<SimpleBookDto>
)