package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import android.content.Context
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.WeatherForecastDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.TimeString
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Fail
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.collection.asList

class WeatherForecastLocalSourceImpl(
    private val dao: WeatherForecastDao,
    private val weatherLocalSrc: WeatherLocalSource,
    private val ctx: Context
): WeatherForecastLocalSource {
    override suspend fun getWeatherForecast(timestamp: TimeString, locationId: Int): Result<WeatherForecast> {
        val entity = dao.getWeatherForecast(timestamp.timeLong, locationId) ?: return Util.noEntityFailResult()
        val weatherRes = weatherLocalSrc.getWeather(entity.weatherId)
        if(weatherRes !is Success)
            return weatherRes as Fail
        val weather = weatherRes.data
        val data = entity.toModel(weather)
        return Success(data, 0)
    }

    override suspend fun getWeatherForecastBatch(startTimestamp: TimeString, locationId: Int): Result<List<WeatherForecast>> {
        val list = dao.getWeatherForecastBatch(startTimestamp.timeLong, locationId).map {
            val weatherRes = weatherLocalSrc.getWeather(it.weatherId)
            if(weatherRes !is Success)
                return weatherRes as Fail
            val weather = weatherRes.data
            it.toModel(weather)
        }
        loge("WethearLocalSrc.getWeatherForecastBatch() startTimestamp= $startTimestamp locationId= $locationId")

        val locationId_q = Util.getSharedPref(ctx).getInt(Const.KEY_LOCATION_ID, -1)
        loge("WethearLocalSrc.getWeatherForecastBatch() locationId_q= $locationId_q")
        return Success(list, 0)
    }

    override suspend fun saveWeatherForecastList(list: List<WeatherForecast>): Result<Int> {
        val locationId = Util.getSharedPref(ctx).getInt(Const.KEY_LOCATION_ID, -1)
        loge("WethearLocalSrc.saveWeatherForecastList() locationId= $locationId")

        val weatherList = list.map { it.weather }.toSet().asList()
        val weatherSaveRes = weatherLocalSrc.saveWeatherList(weatherList)
        if(weatherSaveRes is Fail)
            return weatherSaveRes

        if(locationId < 0)
            return Util.noValueFailResult()
        val entityList = list.map { it.toEntity(locationId) }
        val insertedCount = dao.saveWeatherForecastList(entityList)
        return Util.getInsertResult(insertedCount, entityList.size)
    }
}