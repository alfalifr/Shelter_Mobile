package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.WeatherForecastLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.WeatherForecastRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.WeatherForecast
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.WeatherForecastRepo
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSingleResult
import sidev.lib.android.std.tool.util.`fun`.loge
import java.lang.IllegalArgumentException

class WeatherForecastCompositeSource(
    private val localSrc: WeatherForecastLocalSource,
    private val remoteSrc: WeatherForecastRemoteSource,
): CompositeDataSource<WeatherForecast>(), WeatherForecastRepo {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<WeatherForecast>> = getDataList(localSrc, args.also { loge("WeatherCompositeSrc.getLocalDataList() args = $args") })

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<WeatherForecast>> = getDataList(remoteSrc, args.also { loge("WeatherCompositeSrc.getRemoteDataList() args = $args") })

    private suspend fun getDataList(repo: WeatherForecastRepo, args: Map<String, Any?>): Result<List<WeatherForecast>> {
        val timestamp = args[Const.KEY_TIMESTAMP] as? String ?: throw IllegalArgumentException("args[Const.KEY_TIMESTAMP] == null")
        val locationId = args[Const.KEY_LOCATION_ID] as? Int ?: throw IllegalArgumentException("args[Const.KEY_LOCATION_ID] == null")
        val isSingle = args[Const.KEY_IS_SINGLE] as? Boolean ?: false

        loge("WeatherCompositeSrc.getDataList() timestamp= $timestamp locationId= $locationId isSingle= $isSingle")

        val batchRes = repo.getWeatherForecastBatch(timestamp, locationId)
        return if(!isSingle) batchRes
        else batchRes.toSingleResult().toListResult() //Because weather forecast will always ahead of the `timestamp`.
/*
        return if(!isSingle) repo.getWeatherForecastBatch(timestamp, locationId)
        else repo.getWeatherForecast(timestamp, locationId).toListResult()
 */
    }

    override fun shouldFetch(
        localDataList: List<WeatherForecast>,
        args: Map<String, Any?>
    ): Boolean = localDataList.isEmpty()

    override suspend fun saveDataList(remoteDataList: List<WeatherForecast>): Result<Int> = localSrc.saveWeatherForecastList(remoteDataList)


    override suspend fun getWeatherForecast(
        timestamp: String,
        locationId: Int
    ): Result<WeatherForecast> = getDataList(mapOf(
        Const.KEY_TIMESTAMP to timestamp,
        Const.KEY_LOCATION_ID to locationId,
        Const.KEY_IS_SINGLE to true,
    )).toSingleResult()

    override suspend fun getWeatherForecastBatch(
        startTimestamp: String,
        locationId: Int
    ): Result<List<WeatherForecast>> = getDataList(mapOf(
        Const.KEY_TIMESTAMP to startTimestamp,
        Const.KEY_LOCATION_ID to locationId,
    ))

    override suspend fun saveWeatherForecastList(list: List<WeatherForecast>): Result<Int> = saveDataList(list)
}