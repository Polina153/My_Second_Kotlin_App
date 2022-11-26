package ru.geekbrains.mysecondkotlinapp.ui.viewmodel

import ru.geekbrains.mysecondkotlinapp.model.Weather

sealed class AppState {
    data class Success(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
