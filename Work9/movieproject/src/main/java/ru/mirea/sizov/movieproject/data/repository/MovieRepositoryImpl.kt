package ru.mirea.sizov.movieproject.data.repository

import android.app.Activity
import android.content.Context
import ru.mirea.sizov.movieproject.domain.models.Movie
import ru.mirea.sizov.movieproject.domain.repository.MovieRepository

class MovieRepositoryImpl(context: Activity) : MovieRepository {
    private val sharedPref = context.getPreferences(Context.MODE_PRIVATE)
    override fun saveMovie(movie: Movie): Boolean {
        try {
            with(sharedPref.edit()) {
                putString("1", movie.name)
                apply()
                return true
            }
        } catch (_: Exception) {
            return false
        }
    }

    override fun getMovie(): Movie? {
        val name = sharedPref.getString("1", "None")
        if (name != null) {
            return Movie(1, name)
        } else {
            return null
        }
    }
}