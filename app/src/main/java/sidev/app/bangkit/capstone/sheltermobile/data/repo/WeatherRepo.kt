package sidev.app.bangkit.capstone.sheltermobile.data.repo

import sidev.app.bangkit.capstone.sheltermobile.data.model.WeatherForecast

interface WeatherRepo {
    suspend fun getForecast(timestamp: String): WeatherForecast
}