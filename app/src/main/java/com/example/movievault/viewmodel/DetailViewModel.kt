package com.example.movievault.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movievault.data.model.Movie
import com.example.movievault.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona el estado y la lógica de la pantalla de detalles.
 * Se encarga de cargar una película por su ID, gestionar su estado de favorito
 * y solicitar su eliminación al repositorio.
 */
class DetailViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _movieState = MutableStateFlow<Movie?>(null)
    val movieState: StateFlow<Movie?> = _movieState

    fun loadMovie(id: Int) {
        viewModelScope.launch {
            _movieState.value = repository.getMovieById(id)
        }
    }

    fun toggleFavorite() {
        val currentMovie = _movieState.value ?: return
        viewModelScope.launch {
            val updatedMovie = currentMovie.copy(isFavorite = !currentMovie.isFavorite)
            repository.saveMovie(updatedMovie)
            _movieState.value = updatedMovie
        }
    }

    fun deleteMovie(onDeleted: () -> Unit) {
        val currentMovie = _movieState.value ?: return
        viewModelScope.launch {
            repository.deleteMovie(currentMovie)
            onDeleted()
        }
    }
}
