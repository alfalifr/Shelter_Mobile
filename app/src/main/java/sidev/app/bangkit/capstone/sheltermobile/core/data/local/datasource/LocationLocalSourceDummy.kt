package sidev.app.bangkit.capstone.sheltermobile.core.data.local.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

object LocationLocalSourceDummy: LocationLocalSource {
    override suspend fun getCurrentLocation(): Result<Location> = Success(Dummy.locationList[0], 0)

    override suspend fun saveCurrentLocation(data: Location): Result<Boolean> = Success(true, 0)

    override suspend fun saveLocation(data: Location): Result<Boolean> = Success(true, 0)

    override suspend fun getLocationByName(name: String): Result<Location> = Dummy.locationList.find {
        it.name.equals(name, true)
    }?.let { Success(it, 0) } ?: Util.noEntityFailResult()

    override suspend fun getAllLocation(): Result<List<Location>> = Success(Dummy.locationList, 0)

    override suspend fun getLocation(coordinate: Coordinate): Result<Location> =
        Util.operationNotAvailableFailResult("getLocation(coordinate: Coordinate)")

    override suspend fun getLocationById(id: Int): Result<Location> = Dummy.locationList.find {
        it.id == id
    }?.let { Success(it, 0) } ?: Util.noEntityFailResult()

    override suspend fun saveLocationList(list: List<Location>): Result<Int> = Success(list.size, 0)
}