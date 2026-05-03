package com.example.movievault.data.model

/**
 * Modelo de datos actualizado con el campo isFavorite.
 */
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val genre: String = "Acción",
    val duration: String = "120 min",
    val isFavorite: Boolean = false
)
