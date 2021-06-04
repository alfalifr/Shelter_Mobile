package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast

interface WeatherForecastRepo {
    suspend fun getWeatherForecast(timestamp: String, locationId: Int): Result<WeatherForecast>
    suspend fun getWeatherForecastBatch(startTimestamp: String, locationId: Int): Result<List<WeatherForecast>>
    suspend fun saveWeatherForecastList(list: List<WeatherForecast>): Result<Int>
}