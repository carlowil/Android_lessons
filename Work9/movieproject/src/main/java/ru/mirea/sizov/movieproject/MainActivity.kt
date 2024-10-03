package ru.mirea.sizov.movieproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.mirea.sizov.movieproject.data.repository.MovieRepositoryImpl
import ru.mirea.sizov.movieproject.domain.usercases.GetFavoriteFilmUseCase
import ru.mirea.sizov.movieproject.domain.models.Movie
import ru.mirea.sizov.movieproject.domain.usercases.SaveMovieToFavoriteUseCase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextMovie = findViewById<EditText>(R.id.editTextFilm)
        val textViewMovie = findViewById<TextView>(R.id.filmData)
        val movieRepository = MovieRepositoryImpl(this)
        findViewById<Button>(R.id.buttonSaveFilm).setOnClickListener {
            val result = SaveMovieToFavoriteUseCase(movieRepository).execute(Movie(1, editTextMovie.text.toString()))
            textViewMovie.text = "Saved result $result"
        }
        findViewById<Button>(R.id.buttonShowFilm).setOnClickListener {
            val movie = GetFavoriteFilmUseCase(movieRepository).execute()
            textViewMovie.text = "Loaded result ${movie?.name}"
        }
    }
}