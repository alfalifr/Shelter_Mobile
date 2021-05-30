package sidev.app.bangkit.capstone.sheltermobile.core.data.remote.datasource

import sidev.app.bangkit.capstone.sheltermobile.core.data.dummy.Dummy
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Result
import sidev.app.bangkit.capstone.sheltermobile.core.domain.repo.Success
import sidev.app.bangkit.capstone.sheltermobile.core.util.Util

object LocationRemoteSourceDummy: LocationRemoteSource {
    override suspend fun getAllLocation(): Result<List<Location>> = Success(Dummy.locationList, 0)

    override suspend fun getLocation(coordinate: Coordinate): Result<Location> = Dummy.locationList.find { it.coordinate == coordinate }
        ?.let { Success(it, 0) } ?: Util.noEntityFailResult()

    override suspend fun getLocationById(id: Int): Result<Location> = Dummy.locationList.find { it.id == id }
        ?.let { Success(it, 0) } ?: Util.noEntityFailResult()

    override suspend fun saveLocationList(list: List<Location>): Result<Int> = Util.operationNotAvailableFailResult()
}