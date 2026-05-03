package com.example.movievault.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movievault.data.model.Movie
import com.example.movievault.data.repository.MovieRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

/**
 * Define los estados de la interfaz para la pantalla de películas favoritas.
 * Permite gestionar de forma desacoplada la carga, el éxito con datos, los errores
 * o el estado de lista de favoritos vacía.
 */
sealed class FavoritesUiState {
    object Loading : FavoritesUiState()
    data class Success(val movies: List<Movie>) : FavoritesUiState()
    data class Error(val message: String) : FavoritesUiState()
    object Empty : FavoritesUiState()
}

/**
 * FavoritesViewModel: Responsable de filtrar y exponer únicamente las películas
 * marcadas como favoritas por el usuario. Observa cambios en tiempo real desde la DB.
 */
class FavoritesViewModel(private val repository: MovieRepository) : ViewModel() {
    
    val uiState: StateFlow<FavoritesUiState> = repository.getFavoriteMovies()
        .map { movies ->
            if (movies.isEmpty()) FavoritesUiState.Empty
            else FavoritesUiState.Success(movies)
        }
        .onStart { emit(FavoritesUiState.Loading) }
        .catch { e -> emit(FavoritesUiState.Error(e.message ?: "Error al cargar favoritos")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FavoritesUiState.Loading
        )
}
