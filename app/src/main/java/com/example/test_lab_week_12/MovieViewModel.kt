package com.example.test_lab_week_12

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test_lab_week_12.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            // movieRepository.fetchMovies() returns a Flow. We need to collect it.
            movieRepository.fetchMovies()
                .catch { e ->
                    // Handle any errors that might occur during the flow collection
                    _error.value = "An exception occurred: ${e.message}"
                }
                .collect { movies ->
                    // This block is executed for each value emitted by the Flow.
                    // Here, 'movies' is the List<Movie> you expect.
                    _popularMovies.value = movies
                }
        }
    }
}
