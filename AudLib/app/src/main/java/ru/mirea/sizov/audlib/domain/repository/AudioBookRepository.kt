package ru.mirea.sizov.audlib.domain.repository

import ru.mirea.sizov.audlib.domain.models.AudioBook
import ru.mirea.sizov.audlib.domain.models.Profile

interface AudioBookRepository {
    fun getAllBooks():Array<AudioBook>
    fun getAllFavoriteBooks(profile: Profile):Array<AudioBook>
    fun getAllPopularBooks():Array<AudioBook>
    fun getBookById(id: Int):AudioBook
}