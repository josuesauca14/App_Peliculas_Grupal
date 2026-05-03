package com.example.movievault.data.repository

import com.example.movievault.data.local.MovieDao
import com.example.movievault.data.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio actualizado: Fuente de la Verdad es el DAO (SQLite).
 */
class MovieRepository(private val movieDao: MovieDao) {

    fun getAllMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun getFavoriteMovies(): Flow<List<Movie>> {
        return movieDao.getFavoriteMovies().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getMovieById(id: Int): Movie? {
        return movieDao.getMovieById(id)?.toDomain()
    }

    suspend fun saveMovie(movie: Movie) {
        movieDao.insertMovie(movie.toEntity())
    }

    suspend fun deleteMovie(movie: Movie) {
        movieDao.deleteMovie(movie.toEntity())
    }
}
