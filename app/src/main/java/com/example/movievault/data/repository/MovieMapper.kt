package com.example.movievault.data.repository

import com.example.movievault.data.local.MovieEntity
import com.example.movievault.data.model.Movie

/**
 * Funciones de extensión para realizar el mapeo de datos entre la capa de datos (Entity)
 * y la capa de dominio/presentación (Movie model). Esto asegura la separación de
 * preocupaciones requerida por la arquitectura MVVM.
 */
fun MovieEntity.toDomain(): Movie {
    return Movie(id, title, overview, posterPath, releaseDate, voteAverage, genre, duration, isFavorite)
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(id, title, overview, posterPath, releaseDate, voteAverage, genre, duration, isFavorite)
}
