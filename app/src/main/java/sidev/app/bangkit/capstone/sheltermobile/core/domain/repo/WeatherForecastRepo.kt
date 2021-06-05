package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast

interface WeatherForecastRepo {
    suspend fun getWeatherForecast(timestamp: TimeString, locationId: Int): Result<WeatherForecast>
    suspend fun getWeatherForecastBatch(startTimestamp: TimeString, locationId: Int): Result<List<WeatherForecast>>
    suspend fun saveWeatherForecastList(list: List<WeatherForecast>): Result<Int>
}