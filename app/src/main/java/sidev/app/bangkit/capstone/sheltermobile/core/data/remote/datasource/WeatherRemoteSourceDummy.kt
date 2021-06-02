package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge

object WeatherRemoteSourceDummy: WeatherRemoteSource {
    override suspend fun getWeatherForecast(
        timestamp: String,
        locationId: Int
    ): Result<WeatherForecast> = Success(Dummy.weatherList.random(), 0).also {
        loge("WeatherRemote.getWeatherForecast() data= $it")
    }

    override suspend fun getWeatherForecastBatch(
        startTimestamp: String,
        locationId: Int
    ): Result<List<WeatherForecast>> = Success(Dummy.weatherList, 0)

    override suspend fun saveWeatherForecastList(list: List<WeatherForecast>): Result<Int> = Util.operationNotAvailableFailResult()
}