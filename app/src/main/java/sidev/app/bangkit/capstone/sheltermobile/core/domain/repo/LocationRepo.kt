package sidev.app.bangkit.capstone.sheltermobile.core.domain.repo

import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Coordinate
import sidev.app.bangkit.capstone.sheltermobile.core.domain.model.Location

interface LocationRepo {
/*
    /**
     * [locationName] can be either city, province, or other particular place name.
     */
    suspend fun getLocationList(parentLocationName: String): Result<List<Location>>
 */
    suspend fun getAllLocation(): Result<List<Location>>
    //suspend fun getLocationList(parentLocationCoordinate: Coordinate): Result<List<Location>>
    suspend fun getLocation(coordinate: Coordinate): Result<Location>
    suspend fun getLocationById(id: Int): Result<Location>

    suspend fun saveLocationList(list: List<Location>): Result<Int>
}