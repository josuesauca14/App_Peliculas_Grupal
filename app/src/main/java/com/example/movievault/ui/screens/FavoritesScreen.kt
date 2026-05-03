/**
 * Pantalla que muestra la colección de películas marcadas como favoritas por el usuario.
 * Observa el estado del FavoritesViewModel para mostrar una lista persistida en la
 * base de datos local SQLite. Si no hay favoritos, muestra un mensaje informativo.
 */
package com.example.movievault.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.movievault.R
import com.example.movievault.ui.components.MovieCard
import com.example.movievault.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onMovieClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_favorites)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        if (favoriteMovies.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.empty_favorites))
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(favoriteMovies) { movie ->
                    MovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
                }
            }
        }
    }
}
