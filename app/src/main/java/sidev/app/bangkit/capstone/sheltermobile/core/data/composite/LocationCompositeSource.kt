package sidev.app.bangkit.capstone.sheltermobile.core.data.composite

import sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource.LocationLocalSource
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.LocationRepo
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.util.Const
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toListResult
import sidev.app.bangkit.capstone.sheltermobile.core.util.DataMapper.toSingleResult

class LocationCompositeSource(
    private val localSrc: LocationLocalSource,
): CompositeDataSource<Location>(), LocationRepo {
    override suspend fun getLocalDataList(args: Map<String, Any?>): Result<List<Location>> {
        val id = args[Const.KEY_ID] as? Int
        val coordinate = args[Const.KEY_COORDINATE] as? Coordinate
        return when {
            id != null -> localSrc.getLocationById(id).toListResult()
            coordinate != null -> localSrc.getLocation(coordinate).toListResult()
            else -> localSrc.getAllLocation()
        }
    }

    override suspend fun getRemoteDataList(args: Map<String, Any?>): Result<List<Location>> {
        TODO("Not yet implemented")
    }

    override fun shouldFetch(localDataList: List<Location>, args: Map<String, Any?>): Boolean = localDataList.isEmpty()

    override suspend fun saveDataList(remoteDataList: List<Location>): Result<Int> = localSrc.saveLocationList(remoteDataList)

    override suspend fun getAllLocation(): Result<List<Location>> = getDataList(emptyMap())

    override suspend fun getLocation(coordinate: Coordinate): Result<Location> = getDataList(mapOf(Const.KEY_COORDINATE to coordinate)).toSingleResult()

    override suspend fun getLocationById(id: Int): Result<Location> = getDataList(mapOf(Const.KEY_ID to id)).toSingleResult()

    override suspend fun saveLocationList(list: List<Location>): Result<Int> = saveDataList(list)
}