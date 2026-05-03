/**
 * ViewModel que gestiona la lista de películas favoritas.
 * Escucha los cambios en la base de datos local a través del repositorio
 * y expone un StateFlow con la lista actualizada de favoritos.
 */
package com.example.movievault.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movievault.data.model.Movie
import com.example.movievault.data.repository.MovieRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(repository: MovieRepository) : ViewModel() {
    val favoriteMovies: StateFlow<List<Movie>> = repository.getFavoriteMovies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
