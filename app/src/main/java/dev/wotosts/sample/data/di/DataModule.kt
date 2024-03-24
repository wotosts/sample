package dev.wotosts.sample.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.wotosts.sample.data.repository.BookRepositoryImpl
import dev.wotosts.sample.domain.BookRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindBookRepository(repository: BookRepositoryImpl): BookRepository
}