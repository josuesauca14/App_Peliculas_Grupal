/**
 * Actividad principal de la aplicación que gestiona la navegación y la inicialización
 * de los componentes principales (Base de Datos y Repositorio) utilizando Jetpack Compose.
 */
package com.example.movievault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.movievault.data.local.MovieDatabase
import com.example.movievault.data.repository.MovieRepository
import com.example.movievault.ui.screens.DetailScreen
import com.example.movievault.ui.screens.FavoritesScreen
import com.example.movievault.ui.screens.HomeScreen
import com.example.movievault.ui.screens.MovieFormScreen
import com.example.movievault.ui.theme.MovieVaultTheme
import com.example.movievault.viewmodel.DetailViewModel
import com.example.movievault.viewmodel.FavoritesViewModel
import com.example.movievault.viewmodel.HomeViewModel
import com.example.movievault.viewmodel.MovieFormViewModel
import com.example.movievault.viewmodel.MovieViewModelFactory

/**
 * Clase principal que actúa como punto de entrada de la aplicación.
 * Gestiona la inicialización de la base de datos, el repositorio y la configuración de navegación.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = MovieDatabase.getDatabase(this)
        val repository = MovieRepository(database.movieDao())
        val factory = MovieViewModelFactory(repository)

        setContent {
            MovieVaultTheme {
                MovieApp(factory)
            }
        }
    }
}

@Composable
fun MovieApp(factory: MovieViewModelFactory) {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val viewModel: HomeViewModel = viewModel(factory = factory)
            HomeScreen(
                viewModel = viewModel,
                onMovieClick = { movieId -> navController.navigate("detail/$movieId") },
                onFavoritesClick = { navController.navigate("favorites") },
                onAddClick = { navController.navigate("form") }
            )
        }
        
        composable(
            "detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1
            val viewModel: DetailViewModel = viewModel(factory = factory)
            
            DetailScreen(
                movieId = movieId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onEditClick = { navController.navigate("form?movieId=$movieId") }
            )
        }
        
        composable("favorites") {
            val viewModel: FavoritesViewModel = viewModel(factory = factory)
            FavoritesScreen(
                viewModel = viewModel,
                onMovieClick = { movieId -> navController.navigate("detail/$movieId") },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "form?movieId={movieId}",
            arguments = listOf(navArgument("movieId") { 
                type = NavType.IntType
                defaultValue = -1 
            })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1
            val viewModel: MovieFormViewModel = viewModel(factory = factory)
            
            LaunchedEffect(movieId) {
                if (movieId != -1) {
                    viewModel.loadMovie(movieId)
                }
            }

            MovieFormScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }
    }
}
