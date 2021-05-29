package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast

interface WeatherRepo {
    suspend fun getWeatherForecast(timestamp: String): Result<WeatherForecast>
    suspend fun getWeatherForecastBatch(startTimestamp: String): Result<List<WeatherForecast>>
    suspend fun saveWeatherForecastList(list: List<WeatherForecast>): Result<Int>
}