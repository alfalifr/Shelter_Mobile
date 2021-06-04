package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Weather
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

object WeatherRemoteSourceDummy: WeatherRemoteSource {
    override suspend fun getWeather(id: Int): Result<Weather> = Dummy.weatherList.find { it.id == id }
        ?.let { Success(it, 0) } ?: Util.noValueFailResult()

    override suspend fun getAllWeather(): Result<List<Weather>> = Success(Dummy.weatherList, 0)

    override suspend fun saveWeatherList(list: List<Weather>): Result<Int> = Util.operationNotAvailableFailResult()
}