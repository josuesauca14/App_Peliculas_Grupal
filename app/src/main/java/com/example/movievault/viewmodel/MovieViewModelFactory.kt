/**
 * Factoría personalizada para la creación de instancias de ViewModel.
 * Permite la inyección del repositorio de películas en los ViewModels, facilitando
 * la separación de preocupaciones y cumpliendo con los requisitos de la arquitectura MVVM.
 */
package com.example.movievault.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.movievault.data.repository.MovieRepository

class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(MovieFormViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieFormViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
