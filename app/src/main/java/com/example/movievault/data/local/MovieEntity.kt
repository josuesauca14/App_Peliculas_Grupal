package com.example.movievault.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de base de datos actualizada.
 */
/**
 * Entidad que representa la estructura de la tabla 'movies' en la base de datos local.
 * Define las columnas y el esquema que Room utilizará para persistir los datos de las películas.
 */
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val genre: String,
    val duration: String,
    val isFavorite: Boolean
)
