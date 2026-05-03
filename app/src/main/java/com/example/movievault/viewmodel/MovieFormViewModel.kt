package com.example.movievault.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movievault.data.model.Movie
import com.example.movievault.data.repository.MovieRepository
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona el estado del formulario de películas.
 * Mantiene el estado de cada campo del formulario y se encarga de guardar
 * o actualizar la información en la base de datos a través del repositorio.
 */
class MovieFormViewModel(private val repository: MovieRepository) : ViewModel() {

    var id by mutableStateOf<Int?>(null)
    var title by mutableStateOf("")
    var releaseDate by mutableStateOf("2023-01-01")
    var rating by mutableStateOf("8.0")
    var overview by mutableStateOf("")
    var genre by mutableStateOf("Acción")
    var duration by mutableStateOf("120 min")
    var posterPath by mutableStateOf("")
    var isFavorite by mutableStateOf(false)

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            repository.getMovieById(movieId)?.let { movie ->
                id = movie.id
                title = movie.title
                releaseDate = movie.releaseDate
                rating = movie.voteAverage.toString()
                overview = movie.overview
                genre = movie.genre
                duration = movie.duration
                posterPath = movie.posterPath
                isFavorite = movie.isFavorite
            }
        }
    }

    fun saveMovie(onSuccess: () -> Unit) {
        if (title.isBlank()) return

        val movie = Movie(
            id = id ?: (System.currentTimeMillis() % Int.MAX_VALUE).toInt(),
            title = title,
            overview = overview,
            posterPath = if (posterPath.isBlank()) "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTe-saKTGLyhAE81G2fTHw21yqTh7P6iQqTgA&s" else posterPath,
            releaseDate = releaseDate,
            voteAverage = rating.toDoubleOrNull() ?: 0.0,
            genre = genre,
            duration = duration,
            isFavorite = isFavorite
        )

        viewModelScope.launch {
            repository.saveMovie(movie)
            onSuccess()
        }
    }
}
