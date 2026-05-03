package com.example.movievault.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movievault.data.model.Movie
import com.example.movievault.data.repository.MovieRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * HomeViewModel actualizado: Lee de la Base de Datos.
 */
class HomeViewModel(private val repository: MovieRepository) : ViewModel() {

    // Flujo de datos que viene directamente de SQLite
    val movies: StateFlow<List<Movie>> = repository.getAllMovies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
