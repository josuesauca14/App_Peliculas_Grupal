package com.example.movievault.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz de Acceso a Datos (DAO) para la entidad Movie.
 * Define las operaciones CRUD (Crear, Leer, Actualizar, Borrar) para interactuar
 * con la tabla de películas en la base de datos local SQLite a través de Room.
 */
@Dao
interface MovieDao {
    @Query("SELECT * FROM movies ORDER BY id DESC")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE isFavorite = 1")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovieById(id: Int): MovieEntity?
}
