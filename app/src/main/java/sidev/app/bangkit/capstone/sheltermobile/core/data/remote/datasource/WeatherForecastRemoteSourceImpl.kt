package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.api.WeatherApi
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.data.request.WeatherForecastBody
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge

class WeatherForecastRemoteSourceImpl(private val api: WeatherApi): WeatherForecastRemoteSource {
    override suspend fun getWeatherForecast(
        timestamp: TimeString,
        locationId: Int
    ): Result<WeatherForecast> =
        Util.operationNotAvailableFailResult("WeatherForecastRemoteSourceImpl.getWeatherForecast(...)")

    override suspend fun getWeatherForecastBatch(
        startTimestamp: TimeString,
        locationId: Int
    ): Result<List<WeatherForecast>> {
        //val min = Util.getTimeString()
        val max = Util.getTimeStringStandardOffset(startTimestamp)
        val body = WeatherForecastBody(startTimestamp.time, max.time)

        loge("getWeatherForecastBatch() body= $body")

        val call = api.getWeatherForecast(body)
        val res = call.execute()

        loge("getWeatherForecastBatch() res= $res")
        if(!res.isSuccessful)
            return Fail("Can't get weather forecast for body = $body", res.code(), null)

        val rawList = res.body()!! //TODO Alif 4 Juni 2021: Msh error dari endpoint (kalo 'max' dulu baru 'min', langsung kosong response-nya).
        loge("getWeatherForecastBatch() rawList= $rawList")

        val list = rawList.map { it.toModel() }
        loge("getWeatherForecastBatch() list= $list")
        return Success(list, 0)
    }

    override suspend fun saveWeatherForecastList(list: List<WeatherForecast>): Result<Int> =
        Util.operationNotAvailableFailResult("WeatherForecastRemoteSourceImpl.saveWeatherForecastList(list: List<WeatherForecast>)")
}