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
import sidev.lib.android.std.tool.util.`fun`.loge

class WeatherLocalSourceImpl(private val dao: WeatherDao, private val ctx: Context): WeatherLocalSource {
    override suspend fun getWeatherForecast(timestamp: String, locationId: Int): Result<WeatherForecast> {
        val data = dao.getWeatherForecast(timestamp, locationId)?.toModel() ?: return Util.noEntityFailResult()
        return Success(data, 0)
    }

    override suspend fun getWeatherForecastBatch(startTimestamp: String, locationId: Int): Result<List<WeatherForecast>> {
        val list = dao.getWeatherForecastBatch(startTimestamp, locationId).map { it.toModel() }
        loge("WethearLocalSrc.getWeatherForecastBatch() startTimestamp= $startTimestamp locationId= $locationId")

        val locationId_q = Util.getSharedPref(ctx).getInt(Const.KEY_LOCATION_ID, -1)
        loge("WethearLocalSrc.getWeatherForecastBatch() locationId_q= $locationId_q")
        return Success(list, 0)
    }

    override suspend fun saveWeatherForecastList(list: List<WeatherForecast>): Result<Int> {
        val locationId = Util.getSharedPref(ctx).getInt(Const.KEY_LOCATION_ID, -1)
        loge("WethearLocalSrc.saveWeatherForecastList() locationId= $locationId")
        if(locationId < 0)
            return Util.noValueFailResult()
        val entityList = list.map { it.toEntity(locationId) }
        val insertedCount = dao.saveWeatherForecastList(entityList)
        return Util.getInsertResult(insertedCount, entityList.size)
    }
}