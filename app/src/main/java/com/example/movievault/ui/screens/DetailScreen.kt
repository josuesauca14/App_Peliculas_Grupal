package com.example.movievault.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.movievault.ui.components.MovieRatingBar
import com.example.movievault.viewmodel.DetailUiState
import com.example.movievault.viewmodel.DetailViewModel

/**
 * DetailScreen: Proporciona una vista detallada de una película.
 * Incluye la descripción completa, calificación, género y acciones para
 * editar la información, eliminar la película o alternar su estado de favorito.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int,
    viewModel: DetailViewModel,
    onBack: () -> Unit,
    onEditClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(movieId) {
        viewModel.loadMovie(movieId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is DetailUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is DetailUiState.Error -> {
                Text(
                    text = state.message,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
            is DetailUiState.Success -> {
                val movie = state.movie
                
                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text("Confirmar eliminación") },
                        text = { Text("¿De verdad quieres eliminar esta película?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDeleteDialog = false
                                    viewModel.deleteMovie(onBack)
                                }
                            ) {
                                Text("Eliminar", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDeleteDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Detalles", fontWeight = FontWeight.Bold) },
                            navigationIcon = {
                                IconButton(onClick = onBack) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.secondary)
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { viewModel.toggleFavorite() },
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = Color.White,
                            shape = CircleShape
                        ) {
                            Icon(
                                if (movie.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito"
                            )
                        }
                    }
                ) { padding ->
                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = movie.posterPath,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(250.dp).background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )
                        
                        Column(modifier = Modifier.padding(24.dp)) {
                            Text(text = movie.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
                            MovieRatingBar(rating = movie.voteAverage)
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp)).padding(16.dp)) {
                                InfoItem(icon = "📅", label = "Estreno:", value = movie.releaseDate)
                                InfoItem(icon = "🎭", label = "Género:", value = movie.genre)
                                InfoItem(icon = "⏱", label = "Duración:", value = movie.duration)
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(text = movie.overview, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.tertiary)
                            
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(
                                    onClick = onEditClick,
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Editar", fontWeight = FontWeight.Bold)
                                }
                                
                                OutlinedButton(
                                    onClick = { showDeleteDialog = true },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f)),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Eliminar", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 18.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
