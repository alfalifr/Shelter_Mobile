package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import android.content.Context
import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.WeatherDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class WeatherLocalSourceImpl(private val dao: WeatherDao, private val ctx: Context): WeatherLocalSource {
    override suspend fun getWeatherForecast(timestamp: String): Result<WeatherForecast> {
        val data = dao.getWeatherForecast(timestamp)?.toModel() ?: return Util.noEntityFailResult()
        return Success(data, 0)
    }

    override suspend fun getWeatherForecastBatch(startTimestamp: String): Result<List<WeatherForecast>> {
        val list = dao.getWeatherForecastBatch(startTimestamp).map { it.toModel() }
        return Success(list, 0)
    }

    override suspend fun saveWeatherForecastList(list: List<WeatherForecast>): Result<Int> {
        val locationId = Util.getSharedPref(ctx).getInt(Const.KEY_LOCATION_ID, -1)
        if(locationId < 0)
            return Util.noValueFailResult()
        val entityList = list.map { it.toEntity(locationId) }
        val insertedCount = dao.saveWeatherForecastList(entityList)
        return Util.getInsertResult(insertedCount, entityList.size)
    }
}