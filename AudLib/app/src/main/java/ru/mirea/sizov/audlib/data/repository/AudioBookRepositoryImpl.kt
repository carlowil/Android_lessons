package ru.mirea.sizov.audlib.data.repository

import ru.mirea.sizov.audlib.domain.models.AudioBook
import ru.mirea.sizov.audlib.domain.models.Profile
import ru.mirea.sizov.audlib.domain.repository.AudioBookRepository

class AudioBookRepositoryImpl : AudioBookRepository {
    override fun getAllBooks(): Array<AudioBook> {
        TODO("Not yet implemented")
    }

    override fun getAllFavoriteBooks(profile: Profile): Array<AudioBook> {
        TODO("Not yet implemented")
    }

    override fun getAllPopularBooks(): Array<AudioBook> {
        TODO("Not yet implemented")
    }

    override fun getBookById(id: Int): AudioBook {
        TODO("Not yet implemented")
    }
}