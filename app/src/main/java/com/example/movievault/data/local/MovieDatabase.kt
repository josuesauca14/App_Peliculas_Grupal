package com.example.movievault.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Base de datos persistente de la aplicación utilizando la librería Room.
 * Gestiona la creación de la base de datos SQLite, las migraciones y
 * proporciona acceso a los DAOs.
 */
@Database(entities = [MovieEntity::class], version = 6, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_database"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Se ejecuta solo la primera vez que se crea la BD
                        seedData()
                    }

                    override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                        super.onDestructiveMigration(db)
                        // Se ejecuta si Room borra la BD por cambio de versión
                        seedData()
                    }

                    private fun seedData() {
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val movieDao = database.movieDao()
                                val initialMovies = listOf(
                                    MovieEntity(1, "Oppenheimer", "La historia del físico J. Robert Oppenheimer.", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTe-saKTGLyhAE81G2fTHw21yqTh7P6iQqTgA&s", "2023-07-21", 8.2, "Drama", "180 min", false),
                                    MovieEntity(2, "Barbie", "Vivir en Barbie Land es ser un ser perfecto.", "https://upload.wikimedia.org/wikipedia/commons/5/50/Barbie_%282023_movie_logo%29.png", "2023-07-21", 7.5, "Comedia", "114 min", false),
                                    MovieEntity(3, "Spider-Man", "Miles Morales regresa para una aventura épica.", "https://lumiere-a.akamaihd.net/v1/images/el_sorprendente_hombre_ara_241_a_2_28d4d141.jpeg", "2023-06-02", 8.8, "Animación", "140 min", false)
                                )
                                initialMovies.forEach { movieDao.insertMovie(it) }
                            }
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
