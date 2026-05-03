package com.example.movievault.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movievault.data.model.Movie
import com.example.movievault.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

/**
 * Representa los diferentes estados posibles de la interfaz de usuario en la pantalla principal.
 * - [Loading]: La aplicación está consultando los datos.
 * - [Success]: Los datos se cargaron correctamente; incluye la lista de películas, la búsqueda actual y el género filtrado.
 * - [Error]: Ocurrió un fallo en la obtención de datos.
 * - [Empty]: No existen películas registradas en la base de datos local.
 */
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val movies: List<Movie>,
        val searchQuery: String = "",
        val selectedGenre: String = "Todos"
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
    object Empty : HomeUiState()
}

/**
 * HomeViewModel: Gestiona la lógica de negocio de la pantalla de inicio.
 * Proporciona un estado de UI reactivo que combina la búsqueda por texto y el filtrado por género
 * con los datos provenientes del repositorio de Room.
 */
class HomeViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedGenre = MutableStateFlow("Todos")
    val selectedGenre: StateFlow<String> = _selectedGenre

    // Lista de géneros disponibles para el filtro (podría venir de la DB en el futuro)
    val genres = listOf("Todos", "Acción", "Drama", "Comedia", "Animación", "Terror", "Sci-Fi")

    // Flujo de estados que combina los datos de la DB con los filtros de búsqueda y género
    val uiState: StateFlow<HomeUiState> = combine(
        repository.getAllMovies(),
        _searchQuery,
        _selectedGenre
    ) { movies, query, genre ->
        val filteredMovies = movies.filter { movie ->
            val matchesQuery = movie.title.contains(query, ignoreCase = true)
            val matchesGenre = genre == "Todos" || movie.genre.contains(genre, ignoreCase = true)
            matchesQuery && matchesGenre
        }

        if (movies.isEmpty() && query.isEmpty() && genre == "Todos") {
            HomeUiState.Empty
        } else {
            HomeUiState.Success(
                movies = filteredMovies,
                searchQuery = query,
                selectedGenre = genre
            )
        }
    }
    .onStart { emit(HomeUiState.Loading) }
    .catch { e -> emit(HomeUiState.Error(e.message ?: "Ocurrió un error inesperado")) }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onGenreSelected(genre: String) {
        _selectedGenre.value = genre
    }
}
