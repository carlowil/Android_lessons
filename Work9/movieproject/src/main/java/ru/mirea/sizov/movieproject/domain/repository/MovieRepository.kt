package ru.mirea.sizov.movieproject.domain.repository

import ru.mirea.sizov.movieproject.domain.models.Movie

interface MovieRepository {
    fun saveMovie(movie: Movie): Boolean
    fun getMovie(): Movie?
}