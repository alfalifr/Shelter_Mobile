package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.WeatherLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.WeatherRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Weather
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.WeatherRepo
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSingleResult

class WeatherCompositeSource(
    private val localSrc: WeatherLocalSource,
    private val remoteSrc: WeatherRemoteSource,
): CompositeDataSource<Weather>(), WeatherRepo {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<Weather>> = getDataList(localSrc, args)

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<Weather>> = getDataList(remoteSrc, args)

    override suspend fun saveDataList(remoteDataList: List<Weather>): Result<Int> = localSrc.saveWeatherList(remoteDataList)

    private suspend fun getDataList(repo: WeatherRepo, args: Map<String, Any?>): Result<List<Weather>> {
        val id = args[Const.KEY_ID] as? Int
        return if(id == null) repo.getAllWeather()
        else repo.getWeather(id).toListResult()
    }

    override suspend fun getWeather(id: Int): Result<Weather> = getDataList(mapOf(
        Const.KEY_ID to id
    )).toSingleResult()

    override suspend fun getAllWeather(): Result<List<Weather>> = getDataList(emptyMap())

    override suspend fun saveWeatherList(list: List<Weather>): Result<Int> = saveDataList(list)
}