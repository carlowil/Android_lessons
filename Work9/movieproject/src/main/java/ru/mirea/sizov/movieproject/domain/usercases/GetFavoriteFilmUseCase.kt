package ru.mirea.sizov.movieproject.domain.usercases

import ru.mirea.sizov.movieproject.domain.models.Movie
import ru.mirea.sizov.movieproject.domain.repository.MovieRepository


class GetFavoriteFilmUseCase(private val movieRepository: MovieRepository) {
    fun execute(): Movie? {
        return movieRepository.getMovie()
    }
}