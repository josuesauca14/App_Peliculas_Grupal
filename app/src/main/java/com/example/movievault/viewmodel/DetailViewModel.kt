package com.example.movievault.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movievault.data.model.Movie
import com.example.movievault.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Estados de la UI para la pantalla de detalle de una película.
 * Ayuda a manejar el ciclo de vida de la carga de un registro específico por ID.
 */
sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val movie: Movie) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}

/**
 * DetailViewModel: Gestiona la visualización, eliminación y actualización del estado
 * de favorito de una película individual. Utiliza estados definidos para evitar nulos en la UI.
 */
class DetailViewModel(private val repository: MovieRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadMovie(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            try {
                val movie = repository.getMovieById(id)
                if (movie != null) {
                    _uiState.value = DetailUiState.Success(movie)
                } else {
                    _uiState.value = DetailUiState.Error("Película no encontrada")
                }
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun toggleFavorite() {
        val currentState = _uiState.value
        if (currentState is DetailUiState.Success) {
            viewModelScope.launch {
                val updatedMovie = currentState.movie.copy(isFavorite = !currentState.movie.isFavorite)
                repository.saveMovie(updatedMovie)
                _uiState.value = DetailUiState.Success(updatedMovie)
            }
        }
    }

    fun deleteMovie(onDeleted: () -> Unit) {
        val currentState = _uiState.value
        if (currentState is DetailUiState.Success) {
            viewModelScope.launch {
                repository.deleteMovie(currentState.movie)
                onDeleted()
            }
        }
    }
}
