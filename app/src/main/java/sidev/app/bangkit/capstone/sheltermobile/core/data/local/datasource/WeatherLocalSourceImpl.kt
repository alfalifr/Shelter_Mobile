package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.room.WeatherDao
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Weather
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toEntity
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toModel
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

class WeatherLocalSourceImpl(private val dao: WeatherDao): WeatherLocalSource {
    override suspend fun getWeather(id: Int): Result<Weather> {
        val model = dao.getWeather(id)?.toModel() ?: return Util.noEntityFailResult()
        return Success(model, 0)
    }

    override suspend fun getAllWeather(): Result<List<Weather>> {
        val list = dao.getAllWeathers().map { it.toModel() }
        return Success(list, 0)
    }

    override suspend fun saveWeatherList(list: List<Weather>): Result<Int> {
        val entityList = list.map { it.toEntity() }
        val inserted = dao.saveWeatherList(entityList)
        return Util.getInsertResult(inserted, entityList.size)
    }
}