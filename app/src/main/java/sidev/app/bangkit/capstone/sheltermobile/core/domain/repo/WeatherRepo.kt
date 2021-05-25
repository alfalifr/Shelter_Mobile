package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast

interface WeatherRepo {
    suspend fun getForecast(timestamp: String): Result<WeatherForecast>
    suspend fun getForecastBatch(startTimestamp: String): Result<List<WeatherForecast>>
    suspend fun saveForecastList(list: List<WeatherForecast>): Result<Int>
}