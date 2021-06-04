package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Weather

interface WeatherRepo {
    suspend fun getWeather(id: Int): Result<Weather>
    suspend fun getAllWeather(): Result<List<Weather>>
    suspend fun saveWeatherList(list: List<Weather>): Result<Int>
}