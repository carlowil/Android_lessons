package ru.mirea.sizov.movieproject.domain.usercases

import ru.mirea.sizov.movieproject.domain.models.Movie
import ru.mirea.sizov.movieproject.domain.repository.MovieRepository


class SaveMovieToFavoriteUseCase(private val movieRepository: MovieRepository) {
    fun execute(movie: Movie): Boolean {
        return movieRepository.saveMovie(movie)
    }
}