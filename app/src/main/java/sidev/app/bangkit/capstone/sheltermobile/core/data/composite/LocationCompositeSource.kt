package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.LocationLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource.LocationRemoteSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Disaster
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.DisasterRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.LocationRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSingleResult
import java.lang.IllegalStateException

class LocationCompositeSource(
    private val localSrc: LocationLocalSource,
    private val remoteSrc: LocationRemoteSource,
): CompositeDataSource<Location>(), LocationRepo {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<Location>> = getDataList(localSrc, args)

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<Location>> = getDataList(remoteSrc, args)

    private suspend fun getDataList(repo: LocationRepo, args: Map<String, Any?>): Result<List<Location>> {
        val id = args[Const.KEY_ID] as? Int
        val coordinate = args[Const.KEY_COORDINATE] as? Coordinate
        return when {
            id != null -> repo.getLocationById(id).toListResult()
            coordinate != null -> repo.getLocation(coordinate).toListResult()
            else -> repo.getAllLocation()
        }
    }

    override fun shouldFetch(localDataList: List<Location>, args: Map<String, Any?>): Boolean = localDataList.isEmpty()

    override suspend fun saveDataList(remoteDataList: List<Location>): Result<Int> = localSrc.saveLocationList(remoteDataList)

    override val shouldMapReturnedRemoteData: Boolean = true
    override suspend fun mapNewReturnedData(remoteData: Location, args: Map<String, Any?>): Location {
        val insertEntityRes = localSrc.saveLocation(remoteData)
        if(insertEntityRes !is Success)
            throw IllegalStateException("Can't insert location data ($remoteData)")

        val entityRes = localSrc.getLocationByName(remoteData.name)
        if(entityRes !is Success)
            throw IllegalStateException("Somehow, expected newly inserted entity ($remoteData) can't be inserted ot queried")

        return entityRes.data
    }

    override suspend fun getAllLocation(): Result<List<Location>> = getDataList(emptyMap())

    override suspend fun getLocation(coordinate: Coordinate): Result<Location> = getDataList(mapOf(Const.KEY_COORDINATE to coordinate)).toSingleResult()

    override suspend fun getLocationById(id: Int): Result<Location> = getDataList(mapOf(Const.KEY_ID to id)).toSingleResult()

    override suspend fun saveLocationList(list: List<Location>): Result<Int> = saveDataList(list)
}